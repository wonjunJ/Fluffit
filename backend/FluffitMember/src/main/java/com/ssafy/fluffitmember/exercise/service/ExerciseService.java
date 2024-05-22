package com.ssafy.fluffitmember.exercise.service;

import com.ssafy.fluffitmember._common.exception.InvalidDistanceSpeed;
import com.ssafy.fluffitmember._common.exception.InvalidStartEndTime;
import com.ssafy.fluffitmember._common.exception.NotFoundUserException;
import com.ssafy.fluffitmember.exercise.dto.StepsKafkaDto;
import com.ssafy.fluffitmember.exercise.dto.request.RunningReqDto;
import com.ssafy.fluffitmember.exercise.dto.request.StepsReqDto;
import com.ssafy.fluffitmember.exercise.dto.response.RunningResDto;
import com.ssafy.fluffitmember.exercise.dto.response.StepsResDto;
import com.ssafy.fluffitmember.exercise.entity.Running;
import com.ssafy.fluffitmember.exercise.entity.Steps;
import com.ssafy.fluffitmember.exercise.repository.RunningRepository;
import com.ssafy.fluffitmember.exercise.repository.StepsRepository;
import com.ssafy.fluffitmember.member.entity.Member;
import com.ssafy.fluffitmember.member.repository.MemberRepository;
import com.ssafy.fluffitmember.messagequeue.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final RunningRepository exerciseRepository;
    private final StepsRepository stepsRepository;
    private final MemberRepository memberRepository;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public RunningResDto getRunningReword(String memberId, RunningReqDto runningReqDto) {

        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        if(findMember.isEmpty()){
            throw new NotFoundUserException();
        }
        Member member = findMember.get();
        Running running = Running.of(member,runningReqDto.getStartTime(),runningReqDto.getEndTime(),runningReqDto.getCalorie(),memberId);
        exerciseRepository.save(running);

        int reward = calculateRunningReward(runningReqDto);

        int coin = member.getCoin();
        member.updateCoin(coin + reward);

        return new RunningResDto(reward);
    }

    private int calculateRunningReward(RunningReqDto runningReqDto) {
        int pointsPer100Calories = 10;
        return (runningReqDto.getCalorie() / 100) * pointsPer100Calories;
    }

//    private static final double MINIMUM_ALLOWED_SPEED_KMH = 5.0; // 최소 허용 속도
//    private static final double MAXIMUM_ALLOWED_SPEED_KMH = 20.0; // 최소 허용 속도
      //거리에 따른 리워드
//    private int calculateRunningReward(RunningReqDto runningReq) {
//        // 달린 시간 계산
//        long durationInSeconds = Duration.between(runningReq.getStartTime(), runningReq.getEndTime()).getSeconds();
//        if(durationInSeconds<=0){
//            throw new InvalidStartEndTime();
//        }
//        double durationInHours = durationInSeconds / 3600.0; //달린 시간 계산 -> 시간 단위
//
//        // 평균 속도 계산 (km/h)
//        double averageSpeedKmh = runningReq.getDistance() / durationInHours;
//        if (runningReq.getDistance() <= 0 || averageSpeedKmh < 0) {
//            throw new InvalidDistanceSpeed();
//        }
//        // 기본 보상: 달린 거리에 따라 포인트를 계산
//        int baseReward = (int) (runningReq.getDistance() * 10); // 1km당 10 포인트
//
//        int speedBonus = 0;
//
//        // 속도가 너무 낮으면 보상 없음
//        if (averageSpeedKmh < MINIMUM_ALLOWED_SPEED_KMH) {
//            return baseReward;
//        }
//
//        // 속도에 따른 추가 보상
//        if (averageSpeedKmh >= 10.0 && durationInHours > 0.25) {
//            speedBonus = 50; // 시속 10km 이상 시 50 포인트 추가
//        }
//        if(averageSpeedKmh >= MAXIMUM_ALLOWED_SPEED_KMH){
//            return 0;
//        }
//
//        return baseReward + speedBonus;
//    }

    @Transactional
    public StepsResDto getStepsReword(String memberId, StepsReqDto stepsReqDto) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        if(findMember.isEmpty()){
            throw new NotFoundUserException();
        }
        Member member = findMember.get();
        Optional<Steps> findSteps = stepsRepository.findByMemberIdAndDate(memberId, stepsReqDto.getDate());
        int newSteps = stepsReqDto.getStepCount();
        int currentSteps = 0;
        Steps steps = null;
        if(findSteps.isEmpty()){
            steps = Steps.of(member,stepsReqDto.getDate(),newSteps,memberId);
            stepsRepository.save(steps);
        }else{
            steps = findSteps.get();
            currentSteps = steps.getStepCount();
        }

        // kafka
        StepsKafkaDto stepsKafkaDto = new StepsKafkaDto(member.getMemberId(),newSteps - currentSteps);
        kafkaProducer.send("steps-update",stepsKafkaDto);
        int stepsReword = calculateStepsReward(currentSteps,stepsReqDto.getStepCount());
        int coin = member.getCoin();
        member.updateCoin(coin + stepsReword);
        steps.updateStepCount(stepsReqDto.getStepCount());
        return new StepsResDto(member.getCoin(),stepsReword);
    }

    private int calculateStepsReward(int currentSteps, int newSteps) {

        int currentS = 0;
        if(currentSteps>0){
            currentS = currentSteps/1000;
        }
        int newS = 0;
        if(newSteps>0){
            newS = newSteps/1000;
        }

        int stepDifference = newS - currentS;

        int reword = 0;
        // 걸음수의 앞자리가 바뀌었을 때만 reword 추가
        for (int i = 0; i < stepDifference; i++) {
            reword += 10;
        }

        // 걸음수의 앞자리가 바뀌었고, 걸음수가 10000걸음을 돌파했을 때 보너스
        if(stepDifference>=1 && newS >= 10){  
            reword += 50;
        }
        return reword;
    }
}
