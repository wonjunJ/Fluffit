package com.ssafy.fluffitbattle.service;

import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.entity.BattleRecord;
import com.ssafy.fluffitbattle.entity.BattleType;
import com.ssafy.fluffitbattle.repository.BattleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
@RequiredArgsConstructor
@EnableJpaRepositories
@Slf4j
public class BattleService {

    private final BattleRepository battleRepository;
    private final NotificationService notificationService;
    private final RedisTemplate<String, String> redisTemplate;

    private final Map<Long, Boolean> userReadyStatus = new ConcurrentHashMap<>();
    private final Map<Long, Battle> userBattle = new ConcurrentHashMap<>();


    private Random random = new Random();


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
            notificationService.notifyUser(userId, "Your " + battleType + " battle with User " + opponentId + " is ready!");
            notificationService.notifyUser(Long.parseLong(opponentId), "Your " + battleType + " battle with User " + userId + " is ready!");
            userReadyStatus.put(userId, false);
            userReadyStatus.put(Long.parseLong(opponentId), false);
            userBattle.put(userId, theBattle);
            userBattle.put(Long.parseLong(opponentId), theBattle);
//            notificationService.notifyMatching(battle);
        }
    }

    /* TODO
        1. 배틀 대기 유효 시간 정하기
            레디스 키 만료 시간이랑 sse 세션 연결 시간이랑 맞추고
        2. 키(유저 아이디) 중복 방지, 따닥 방지 - 이를 위해서는 대기 큐 말고 따로 저장 공간이 필요할 듯, 분산 락 사용
        3. 연결 상태 감지해서 연결 끊기거나 네트워크 불안정으로 배틀 참여 어려우면 대기 큐에서 빼기
        4. 동시에 배틀 여러개 진행 못하게 하기 (유효 기간 지났더라도 컨펌을 동시에 할 수도 있으니)
            대결 중이거나 컨펌 대기 중인 유저 : 배틀 아이디 저장하는 곳도 있어야 할 듯
        + ) 레디 상태 관련 테스트
        5. 랭킹 산출은 스프링 배치 - 분산 처리 반드시
        6. 빨리 해내면 reactive
     */

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

    // 배틀 결과 저장 및 알림
    private void processBattleResult(Long battleId, Map<Long, BattleRecord> records) {
        Battle battle = battleRepository.findById(battleId).orElseThrow();
        BattleRecord record1 = records.values().stream().findFirst().get();
        BattleRecord record2 = records.values().stream().skip(1).findFirst().get();

        // 승자 결정 로직
        Long winnerId = record1.getScore() > record2.getScore() ? record1.getUserId() : record2.getUserId();
        battle.setWinnerId(winnerId);
        battle.setBattleDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        battleRepository.save(battle);

        /* TODO
            랭킹에 적용
         */

        // 사용자에게 결과 알림
        notificationService.notifyUser(record1.getUserId(), "Battle result: " + (winnerId.equals(record1.getUserId()) ? "Win!" : "Lose!"));
        notificationService.notifyUser(record2.getUserId(), "Battle result: " + (winnerId.equals(record2.getUserId()) ? "Win!" : "Lose!"));
    }




}
