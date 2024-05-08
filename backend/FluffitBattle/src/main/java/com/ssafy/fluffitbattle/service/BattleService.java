package com.ssafy.fluffitbattle.service;

import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.entity.BattleRecord;
import com.ssafy.fluffitbattle.entity.BattleType;
import com.ssafy.fluffitbattle.repository.BattleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BattleService {

    private final BattleRepository battleRepository;
    private final NotificationService notificationService;
    private final RedisTemplate<String, String> redisTemplate;

    private final Map<Long, Boolean> userReadyStatus = new ConcurrentHashMap<>();
    private final Map<Long, Battle> userBattle = new ConcurrentHashMap<>();


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
            Battle theBattle = battleRepository.save(battle); // 게임 설명도 보내야하는지
            notificationService.notifyUser(userId, "Your " + battleType + " battle is ready!");
            notificationService.notifyUser(Long.parseLong(opponentId), "Your " + battleType + " battle is ready!");
            userReadyStatus.put(userId, false);
            userReadyStatus.put(Long.parseLong(opponentId), false);
            userBattle.put(userId, theBattle);
            userBattle.put(Long.parseLong(opponentId), theBattle);
//            notificationService.notifyMatching(battle);
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

    // 둘 다 레디 상태면 배틀 시작
    private void startBattle(Long userId1, Long userId2) {
//        userReadyStatus.put(userId1, false);
//        userReadyStatus.put(userId2, false);
//
//        notificationService.notifyUser(userId1, "Your opponent is ready! Confirm to start the battle.");
//        notificationService.notifyUser(userId2, "Your opponent is ready! Confirm to start the battle.");
        notificationService.notifyUser(userId1, "game started");
        notificationService.notifyUser(userId2, "game started");

    }

    // 레디 상태 눌렀는지
    public void confirmBattle(Long userId) {
        userReadyStatus.put(userId, true);
        Battle battle = userBattle.get(userId);
        Long opponentId = battle.getOrganizerId();
        if (opponentId == userId) opponentId = battle.getParticipantId();
        if (userReadyStatus.get(opponentId)) startBattle(userId, opponentId);
    }

    public void submitBattleRecord(Long userId, Long record) {
        BattleRecord battleRecord = new BattleRecord(userId, record);
        Long battleId = userBattle.get(userId).getId();
        String key = "battle:" + battleId;
        HashOperations<String, Long, BattleRecord> hashOps = redisTemplate.opsForHash();
        hashOps.put(key, userId, battleRecord);

        if (hashOps.size(key) == 2) { // 두 사용자의 결과가 모두 도착했을 때
            Map<Long, BattleRecord> records = hashOps.entries(key);
            processBattleResult(battleId, new HashMap<>(records));
            redisTemplate.delete(key); // 처리 후 레코드 삭제
        }
    }

    private void processBattleResult(Long battleId, Map<Long, BattleRecord> records) {
        Battle battle = battleRepository.findById(battleId).orElseThrow();
        BattleRecord record1 = records.values().stream().findFirst().get();
        BattleRecord record2 = records.values().stream().skip(1).findFirst().get();

        // 승자 결정 로직 (예시로 점수 비교)
        Long winnerId = record1.getScore() > record2.getScore() ? record1.getUserId() : record2.getUserId();
        battle.setWinnerId(winnerId);
        battleRepository.save(battle);

        // 사용자에게 결과 알림
        NotificationService notificationService = new NotificationService(); // 예시로 직접 생성
        notificationService.notifyUser(record1.getUserId(), "Battle result: " + (winnerId.equals(record1.getUserId()) ? "Win!" : "Lose!"));
        notificationService.notifyUser(record2.getUserId(), "Battle result: " + (winnerId.equals(record2.getUserId()) ? "Win!" : "Lose!"));
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
