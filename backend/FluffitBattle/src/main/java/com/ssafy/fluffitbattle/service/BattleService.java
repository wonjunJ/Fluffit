package com.ssafy.fluffitbattle.service;

import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.entity.BattleType;
import com.ssafy.fluffitbattle.repository.BattleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BattleService {

    private final BattleRepository battleRepository;
    private final NotificationService notificationService;
    private final RedisTemplate<String, String> redisTemplate;

    private Random random = new Random();

//    public Battle createBattle(Battle battle) {
//        return battleRepository.save(battle);
//    }

    // 매칭 요청 처리
    public void requestBattle(Long userId) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        String opponentId = listOps.leftPop("battleQueue");

        if (opponentId == null) {
            listOps.rightPush("battleQueue", userId.toString());
            notificationService.notifyUser(userId, "Waiting for an opponent...");
        } else {
            BattleType battleType = BattleType.values()[random.nextInt(BattleType.values().length)];
            Battle battle = Battle.builder()
                    .organizerId(Long.parseLong(opponentId))
                    .participantId(userId)
                    .battleType(battleType)
                    .build();
            battleRepository.save(battle); // 게임 설명도 보내야하는지
            notificationService.notifyUser(userId, "Your " + battleType + " battle is ready!");
            notificationService.notifyUser(Long.parseLong(opponentId), "Your " + battleType + " battle is ready!");
            /* TODO
                1. notify 해서 사용자에게 알림을 보낸 후 시작 신호가 오는 거 접수
                2. 결정된 게임 타입에 따라 게임 정보 전송...은 서버에서 안 해도 되는구나
                3. 실시간으로 내 상황 보내면서 상대 상황도 받기(웹소켓으로)
                    - 그걸 레디스에 쭉 저장해뒀다가? 아니면 끝날 때 다시 받아올지
                    - 끝날 때 다시 받아오는 게 맞을 듯. 그러면 레디스에 저장을 하는 게 맞나?
                    - 어디로 받아와
             */
        }
    }

    // 배틀 결과 저장 및 알림
    @Transactional
    public void finishBattle(Long battleId, String result) {
        Battle battle = battleRepository.findById(battleId).orElseThrow();

        /* TODO
            1. 저기 result가 어디서 오는지(redis)
                그리고 그 result를 가지고 배틀 타입에 따라 어떤 식으로 위너를 판별할지
            2. 위너 아이디 세팅 해주고
            3. 판별 결과 You Win!, You Lose! 보내고
            4. 랭킹에 적용
         */
//        battle.setWinnerId(result);
        battle.setBattleDate(LocalDateTime.now());
        battleRepository.save(battle);
        notificationService.notifyUser(battle.getOrganizerId(), "Battle result: " + result);
        notificationService.notifyUser(battle.getParticipantId(), "Battle result: " + result);
    }

}
