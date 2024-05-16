package com.ssafy.fluffitbattle.service;

import com.ssafy.fluffitbattle.client.FlupetFeignClient;
import com.ssafy.fluffitbattle.client.MemberFeignClient;
import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.entity.BattleType;
import com.ssafy.fluffitbattle.entity.dto.*;
import com.ssafy.fluffitbattle.kafka.KafkaProducer;
import com.ssafy.fluffitbattle.repository.BattleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
@RequiredArgsConstructor
//@EnableJpaRepositories
@Slf4j
public class BattleService {

    private final BattleRepository battleRepository;
    private final NotificationService notificationService;
    private final MemberFeignClient memberFeignClient;
    private final FlupetFeignClient flupetFeignClient;
    private final KafkaProducer kafkaProducer;

    @Qualifier("stringRedisTemplate")
    private final RedisTemplate<String, String> stringRedisTemplate;
//    private final RedisTemplate<String, Object> redisTemplate;
    @Qualifier("battleRedisTemplate")
    private final RedisTemplate<String, Battle> battleRedisTemplate;

    @Qualifier("objectRedisTemplate")
    private final RedisTemplate<String, Object> objectRedisTemplate;

    @Qualifier("userBattleLongRedisTemplate")
    private final RedisTemplate<String, Long> userBattleLongRedisTemplate;
//    @Qualifier("userBattleObjectRedisTemplate")
//    private final RedisTemplate<String, String> userBattleObjectRedisTemplate;

    //    @Qualifier("longStringRedisTemplate")
//    private final RedisTemplate<Long, String> longStringRedisTemplate;
    @Qualifier("waitingQueueRedisTemplate")
    private final RedisTemplate<Long, LocalDateTime> waitingQueueRedisTemplate;

    private static final String USER_BATTLE_KEY = "user_battle";
    private static final String BATTLE_QUEUE_KEY = "battle_queue";
    private static final String BATTLE_LIST_KEY = "battle_list";

    private static final String WAIT_MATCHING_EVENTNAME = "wait_matching";
    private static final String ALREADY_IN_MATCHING_EVENTNAME = "already_in_matching";
    private static final String PET_DOES_NOT_EXIST_EVENTNAME = "pet_does_not_exist";
    private static final String JUST_NOW_MATCHED_EVENTNAME = "just_now_matched";
    private static final String BATTLE_RESULT_EVENTNAME = "battle_result";

    private Random random = new Random();


    // 매칭 요청 처리
    public void requestBattle(String userId) {
        log.info("리퀘스트배틀 진입 "+ userId);

        if (flupetFeignClient.getFlupetInfo(userId).getFlupetImageUrl() == null) {
            notificationService.notifyUser(userId, PET_DOES_NOT_EXIST_EVENTNAME, "");
            return;
        }

        if (getUserBattle(userId) != null) {
            notificationService.notifyUser(userId, ALREADY_IN_MATCHING_EVENTNAME, "");
            return;
        }

        boolean success = false;

        int retryCount = 0;
        int maxRetries = 5;

        while (!success && retryCount < maxRetries) {
            retryCount++;
            AtomicBoolean shouldRetry = new AtomicBoolean(false);

            try {
                List<Object> results = stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
                    @Override
                    public List<Object> execute(RedisOperations operations) throws DataAccessException {
                        operations.watch(BATTLE_QUEUE_KEY); // 대기 큐에

                        ListOperations<String, String> listOps = operations.opsForList();
                        String opponentId = listOps.leftPop(BATTLE_QUEUE_KEY);

                        operations.multi(); // 레디스 트랜잭션 큐에 쌓기 시작
                        log.info("워치, leftPop, 멀티 설정도 성공");

                        if (opponentId == null || getUserBattle(opponentId) != null || userId.equals(opponentId)) {
                            shouldRetry.set(true);
                            operations.opsForList().rightPush(BATTLE_QUEUE_KEY, userId);
                            operations.expire(BATTLE_QUEUE_KEY, 1, TimeUnit.MINUTES);
                            log.info(userId + " 배틀큐에 들어갔어요");
                            logCurrentQueueState(BATTLE_QUEUE_KEY); // Redis에 값이 정상적으로 추가되었는지 확인
                        }
//                        else if (userId.equals(opponentId)) {
//                            operations.opsForList().rightPush(BATTLE_QUEUE_KEY, opponentId);
//                            shouldRetry.set(false);
//                        } else if (flupetFeignClient.getFlupetInfo(userId).getFlupetImageUrl() == null) {
//                            System.out.println(" 다시 큐에 원상복구해 " + opponentId);
//                            operations.opsForList().rightPush(BATTLE_QUEUE_KEY, opponentId);
//                            notificationService.notifyUser(userId, PET_DOES_NOT_EXIST_EVENTNAME, "");
//                            shouldRetry.set(false);
//                        }
                        else {
                            shouldRetry.set(!createAndNotifyBattle(userId, opponentId)); // setBattle 결과에 따라 재시도 설정
                        }

                        return operations.exec(); // 트랜잭션 완료
                    }
                });

                log.info("execute 실행 여부 " + (results.isEmpty() ? " 아니요" : results.get(0)));

                if (results == null || (results.isEmpty() && shouldRetry.get())) {
                    log.info("Transaction failed, retrying...");
                    stringRedisTemplate.unwatch(); // 트랜잭션 실패 시 unwatch 호출
                } else {
                    success = true; // 트랜잭션 성공 시 루프 종료
                }

            } catch (Exception e) {
                log.info("requestBattle이 우리에게 보낸 시련");
                e.printStackTrace();
                stringRedisTemplate.unwatch(); // 예외 발생 시 unwatch 호출
            }
        }

        log.info("리퀘스트 배틀 메서드가 끝날 때의 로그");
        logCurrentQueueState(BATTLE_QUEUE_KEY);
    }

    private boolean createAndNotifyBattle(String userId, String opponentId) {
        log.info("배틀 매칭 : {} vs {}", userId, opponentId);
        BattleType battleType = BattleType.values()[random.nextInt(BattleType.values().length)];
        Battle battle = Battle.builder()
                .organizerId(opponentId)
                .participantId(userId)
                .battleType(battleType)
                .build();
        Battle theBattle = battleRepository.save(battle);
        Long battleId = theBattle.getId();

        notifyJustMatched(userId, opponentId, theBattle);
        notifyJustMatched(opponentId, userId, theBattle);

        setUser(userId, battleId);
        setUser(opponentId, battleId);

        return setBattle(theBattle);
    }

    private void logCurrentQueueState(String key) { // 큐 상태 로깅
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        Long queueSize = listOps.size(key); // 큐의 현재 크기를 가져옵니다.
        if (queueSize == null) queueSize = 0L;
        List<String> queueContents = listOps.range(key, 0, queueSize); // 큐의 전체 내용을 가져옵니다.
        log.info("Current queue size: {}", queueSize);
        log.info("Queue contents: {}", queueContents);
    }

    private void notifyJustMatched(String userId, String opponentId, Battle battle) {
        FlupetInfoClientDto opponentFlupetInfoDto = flupetFeignClient.getFlupetInfo(opponentId);

        String imgUrl;
        if (opponentFlupetInfoDto.getFlupetImageUrl() != null) {
            imgUrl = opponentFlupetInfoDto.getFlupetImageUrl();
        } else {
            imgUrl = "";
        }

        notificationService.notifyUser(userId, JUST_NOW_MATCHED_EVENTNAME,
                BattleMatchingResponseDto.builder()
                        .result(true)
                        .opponentName(memberFeignClient.getNickName(opponentId).getNickname())
                        .opponentBattlePoint(memberFeignClient.getBattlePoint(opponentId).getPoint())
                        .opponentFlupetName(opponentFlupetInfoDto.getFlupetNickname() == null ? "" : opponentFlupetInfoDto.getFlupetNickname())
                        .opponentFlupetImageUrl(imgUrl)
                        .battleId(battle.getId())
                        .battleType(battle.getBattleType())
                        .build());
    }

    private String getUserBattle(String userId) {
        Object result = objectRedisTemplate.opsForHash().get(USER_BATTLE_KEY, userId);
        return result != null ? result.toString() : null;
    }

    private Battle getBattle(String battleKey) {
        Object result = battleRedisTemplate.opsForValue().get(battleKey);
        return result != null ? (Battle) result : null;
    }

    private void setUser(String userId, Long battleId) {
        try {
            userBattleLongRedisTemplate.opsForValue().set("User:" + userId, battleId, 80, TimeUnit.SECONDS);
            objectRedisTemplate.opsForHash().put(USER_BATTLE_KEY, userId, "Battle:" + battleId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean setBattle(Battle battle) {
        try {
            battleRedisTemplate.opsForValue().set("Battle:" + battle.getId(), battle, 1, TimeUnit.HOURS);
            return true;
        } catch (Exception e) {
            log.error("Failed to set battle in Redis", e);
            return false;
        }
    }


    private void writeRecord(String battleKey, String userId, Integer score) {
        Battle battle = getBattle(battleKey);
        boolean isOpponentSubmitted = false;
        assert battle != null;
        if (userId.equals(battle.getOrganizerId())) {
            battle.setOrganizerScore(score);
            isOpponentSubmitted = battle.getParticipantScore() != null;
        } else {
            battle.setParticipantScore(score);
            isOpponentSubmitted = battle.getOrganizerScore() != null;
        }

        battleRedisTemplate.opsForValue().set(battleKey, battle);
        if (isOpponentSubmitted) calculateWinnerAndNotifyResult(battleKey);
    }

    public void submitBattleRecord(String userId, BattleResultRequestDto requestDto) {
        String battleKey = "Battle:" + requestDto.getBattleId();
        writeRecord(battleKey, userId, requestDto.getScore());
    }

    private void calculateWinnerAndNotifyResult(String battleKey) {
        Battle battle = getBattle(battleKey);
        if (battle == null) return;  // Null 체크 추가

        // 승자 결정 및 카프카 메시지 생성
        determineWinnerAndUpdatePoints(battle);

        // 배틀 정보 업데이트 및 저장
        updateAndSaveBattle(battle);

        // Redis와 관련된 클린업 작업
        cleanUpRedisEntries(battle, battleKey);

        // 결과 통지
        notifyResults(battle);
    }

    private void determineWinnerAndUpdatePoints(Battle battle) {
        int organizerScore = battle.getOrganizerScore();
        int participantScore = battle.getParticipantScore();
        String organizerId = battle.getOrganizerId();
        String participantId = battle.getParticipantId();

        BattlePointKafkaDto organizerKafkaDto, participantKafkaDto;

        if (organizerScore > participantScore) {
            battle.setWinnerId(organizerId);
            organizerKafkaDto = new BattlePointKafkaDto(organizerId, 10);
            participantKafkaDto = new BattlePointKafkaDto(participantId, -5);
        } else if (organizerScore < participantScore) {
            battle.setWinnerId(participantId);
            organizerKafkaDto = new BattlePointKafkaDto(organizerId, -5);
            participantKafkaDto = new BattlePointKafkaDto(participantId, 10);
        } else {
            organizerKafkaDto = new BattlePointKafkaDto(organizerId, -5);
            participantKafkaDto = new BattlePointKafkaDto(participantId, -5);
        }

        kafkaProducer.send("battle-point-update", organizerKafkaDto);
        kafkaProducer.send("battle-point-update", participantKafkaDto);
    }

    private void updateAndSaveBattle(Battle battle) {
        battle.setBattleDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        battleRepository.save(battle);
    }

    private void cleanUpRedisEntries(Battle battle, String battleKey) {
        stringRedisTemplate.delete(battleKey);
        stringRedisTemplate.delete("User:" + battle.getOrganizerId());
        stringRedisTemplate.delete("User:" + battle.getParticipantId());
        objectRedisTemplate.opsForHash().delete(USER_BATTLE_KEY, battle.getOrganizerId());
        objectRedisTemplate.opsForHash().delete(USER_BATTLE_KEY, battle.getParticipantId());
    }

    private void notifyResults(Battle battle) {
        String winnerId = battle.getWinnerId();
        notifyBattleResult(battle.getOrganizerId(), battle, battle.getOrganizerId().equals(winnerId));
        notifyBattleResult(battle.getParticipantId(), battle, battle.getParticipantId().equals(winnerId));
    }

    private void notifyBattleResult(String userId, Battle battle, boolean isWinner) {
        notificationService.notifyUser(userId, BATTLE_RESULT_EVENTNAME,
                BattleResultResponseDto.builder()
                        .isWin(isWinner)
                        .battlePoint(Long.valueOf(memberFeignClient.getBattlePoint(userId).getPoint()))
                        .battlePointChanges(isWinner ? 10 : -5)
                        .myBattleScore(Objects.equals(battle.getOrganizerId(), userId) ? battle.getOrganizerScore() : battle.getParticipantScore())
                        .opponentBattleScore(Objects.equals(battle.getOrganizerId(), userId) ? battle.getParticipantScore() : battle.getOrganizerScore())
                        .build());
    }


    public void handleTimeout(String userId) {
        log.info("만료 유저 " + userId);
        String battleKey = getUserBattle(userId);

        if (battleKey == null) return;
        Battle battle = getBattle(battleKey);

        boolean battleNull = battle == null;
        boolean organizer = Objects.equals(battle.getOrganizerId(), userId) && battle.getOrganizerScore() != null;
        boolean participant = Objects.equals(battle.getParticipantId(), userId) && battle.getParticipantScore() != null;

        if (!battleNull && !organizer && !participant) {
            writeRecord(battleKey, userId, -1);
        }

        objectRedisTemplate.opsForHash().delete(USER_BATTLE_KEY, userId);
    }

    public Slice<BattleRecordItemDto> getBattleRecords(String userId, Pageable pageable) {
        return battleRepository.findByOrganizerIdOrParticipantIdOrderByBattleDateDesc(userId, userId, pageable)
                .map(battle -> convertToDto(battle, userId));
    }

    private BattleRecordItemDto convertToDto(Battle battle, String userId) {
        boolean isOrganizer = userId.equals(battle.getOrganizerId());
        String opponentId = isOrganizer ? battle.getParticipantId() : battle.getOrganizerId();
        boolean isWin = userId.equals(battle.getWinnerId());

        return BattleRecordItemDto.builder()
                .isWin(isWin)
                .title(battle.getBattleType().getTitle())
                .opponentName(memberFeignClient.getNickName(opponentId).getNickname())
                .opponentScore(isOrganizer ? battle.getParticipantScore() : battle.getOrganizerScore())
                .myScore(isOrganizer ? battle.getOrganizerScore() : battle.getParticipantScore())
                .date(battle.getBattleDate())
                .build();
    }

    public List<BattleStatisticItemDto> getBattleStats(String userId) {
        List<BattleStatisticItemDto> stats = battleRepository.findBattleStatsByUserId(userId);
        stats.forEach(BattleStatisticItemDto::calculateWinRate);
        return stats;
    }

    public BattleStatisticResponseDto getBattleStaticsResponse(String userId) {
        BattleStatisticResponseDto battleStatisticResponseDto = new BattleStatisticResponseDto();
        battleStatisticResponseDto.setBattlePoint(Long.valueOf(memberFeignClient.getBattlePoint(userId).getPoint()));
        battleStatisticResponseDto.setBattleStatisticItemDtoList(getBattleStats(userId));
        return battleStatisticResponseDto;
    }
}
