package com.ssafy.fluffitbattle.service;

import com.ssafy.fluffitbattle.client.MemberFeignClient;
import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.entity.BattleType;
import com.ssafy.fluffitbattle.entity.dto.BattleMatchingResponseDto;
import com.ssafy.fluffitbattle.entity.dto.BattleResultRequestDto;
import com.ssafy.fluffitbattle.entity.dto.BattleResultResponseDto;
import com.ssafy.fluffitbattle.repository.BattleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
//@EnableJpaRepositories
@Slf4j
public class BattleService {

    private final BattleRepository battleRepository;
    private final NotificationService notificationService;
    private final MemberFeignClient memberFeignClient;

    @Qualifier("stringRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;
    @Qualifier("battleRedisTemplate")
    private final RedisTemplate<String, Battle> battleRedisTemplate;
//    @Qualifier("longStringRedisTemplate")
//    private final RedisTemplate<Long, String> longStringRedisTemplate;
    @Qualifier("waitingQueueRedisTemplate")
    private final RedisTemplate<Long, LocalDateTime> waitingQueueRedisTemplate;

    private static final String USER_BATTLE_KEY = "user_battle";
    private static final String BATTLE_QUEUE_KEY = "battle_queue";
    private static final String BATTLE_LIST_KEY = "battle_list";

    private static final String WAIT_MATCHING_EVENTNAME = "wait_matching";
    private static final String ALREADY_IN_MATCHING_EVENTNAME = "already_in_matching";
    private static final String JUST_NOW_MATCHED_EVENTNAME = "just_now_matched";
    private static final String BATTLE_RESULT_EVENTNAME = "battle_result";

//    private final Map<Long, Boolean> userReadyStatus = new ConcurrentHashMap<>();
//    private final Map<Long, Battle> userBattle = new ConcurrentHashMap<>();


    private Random random = new Random();


    // 매칭 요청 처리
    public void requestBattle(Long userId) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        String opponentId = listOps.leftPop(BATTLE_QUEUE_KEY);

        if (opponentId == null) {
            listOps.rightPush(BATTLE_QUEUE_KEY, userId.toString());
            redisTemplate.expire(BATTLE_QUEUE_KEY, 1, TimeUnit.MINUTES);
            notificationService.notifyUser(userId, WAIT_MATCHING_EVENTNAME,
                    BattleMatchingResponseDto.builder().result(false).build());
        } else if (userId == Long.parseLong(opponentId)) {
            notificationService.notifyUser(userId, WAIT_MATCHING_EVENTNAME,
                    BattleMatchingResponseDto.builder().result(false).build());
        } else if (getUserBattle(userId) != null) {
            notificationService.notifyUser(userId, ALREADY_IN_MATCHING_EVENTNAME,
                    BattleMatchingResponseDto.builder().result(false).build());
        } else {
//            notificationService.removeUserEmitter(userId);
//            notificationService.removeUserEmitter(Long.parseLong(opponentId));
//            notificationService.createBattleEmitter(userId);
//            notificationService.createBattleEmitter(Long.parseLong(opponentId));

            BattleType battleType = BattleType.values()[random.nextInt(BattleType.values().length)];
            Battle battle = Battle.builder()
                    .organizerId(Long.parseLong(opponentId))
                    .participantId(userId)
                    .battleType(battleType)
                    .build();
            Battle theBattle = battleRepository.save(battle);
            Long battleId = theBattle.getId();

            notifyJustMatched(userId, opponentId, theBattle);
            notifyJustMatched(Long.parseLong(opponentId), userId + "", theBattle);

            setUser(userId, battleId);
            setUser(Long.parseLong(opponentId), battleId);

            setBattle(theBattle);
        }
    }

    private void notifyJustMatched(Long userId, String opponentId, Battle battle) {
        notificationService.notifyUser(userId, JUST_NOW_MATCHED_EVENTNAME,
                BattleMatchingResponseDto.builder()
                        .result(true)
                        .opponentName(memberFeignClient.getNickName(opponentId).getNickname())
                        .battleId(battle.getId())
                        .battleType(battle.getBattleType())
                        .build());
    }

    private String getUserBattle(Long userId) {
        Object result = redisTemplate.opsForHash().get(USER_BATTLE_KEY, userId.toString());
        return result != null ? result.toString() : null;
    }

    private Battle getBattle(String battleKey) {
        Object result = battleRedisTemplate.opsForValue().get(battleKey);
        return result != null ? (Battle) result : null;
    }

//    private String getUser(Long userId) {
//        Object result = redisTemplate.opsForValue().get("User:" + userId);
//        return result != null ? result.toString() : null;
//    }

    private void setUser(Long userId, Long battleId) {
        redisTemplate.opsForValue().set("User:" + userId, "Battle:" + battleId, 80, TimeUnit.SECONDS);
        redisTemplate.opsForHash().put(USER_BATTLE_KEY, userId.toString(), "Battle:" + battleId);
    }

    private void setBattle(Battle battle) {
        battleRedisTemplate.opsForValue().set("Battle:" + battle.getId(), battle, 1, TimeUnit.HOURS);
    }




    private void writeRecord(String battleKey, Long userId, Integer score) {
        Battle battle = getBattle(battleKey);
        boolean isOpponentSubmitted = false;
        if (userId == battle.getOrganizerId()) {
            battle.setOrganizerScore(score);
            isOpponentSubmitted = battle.getParticipantScore() != null;
        } else {
            battle.setParticipantScore(score);
            isOpponentSubmitted = battle.getOrganizerScore() != null;
        }
        battleRedisTemplate.opsForValue().set(battleKey, battle);
        if (isOpponentSubmitted) calculateWinnerAndNotifyResult(battleKey);
    }

    public void submitBattleRecord(Long userId, BattleResultRequestDto requestDto) {
        String battleKey = "Battle:" + requestDto.getBattleId();
        writeRecord(battleKey, userId, requestDto.getScore());
    }

    private void calculateWinnerAndNotifyResult(String battleKey) {
        Battle battle = getBattle(battleKey);

        Long organizerId = battle.getOrganizerId();
        Long participantId = battle.getParticipantId();

        /* TODO
            1. winner 배틀 점수 더해주기
         */

        if (battle.getOrganizerScore() > battle.getParticipantScore()) {
            battle.setWinnerId(organizerId);

        } else if (battle.getOrganizerScore() < battle.getParticipantScore()) {
            battle.setWinnerId(participantId);
        }

        battle.setBattleDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        battleRepository.save(battle);
        redisTemplate.delete(battleKey);

        redisTemplate.delete("User:" + organizerId);
        redisTemplate.delete("User:" + participantId);

        redisTemplate.opsForHash().delete(USER_BATTLE_KEY, organizerId.toString());
        redisTemplate.opsForHash().delete(USER_BATTLE_KEY, participantId.toString());

        notifyBattleResult(organizerId, battle);
        notifyBattleResult(participantId, battle);
    }

    private void notifyBattleResult(Long userId, Battle battle) {
        boolean isWin = Objects.equals(battle.getWinnerId(), userId);
        notificationService.notifyUser(userId, BATTLE_RESULT_EVENTNAME,
                BattleResultResponseDto.builder()
                .isWin(isWin)
                .myBattleScore(battle.getOrganizerId() == userId ? battle.getOrganizerScore() : battle.getParticipantScore())
                .opponentBattleScore(battle.getOrganizerId() == userId ? battle.getParticipantScore() : battle.getOrganizerScore())
                .build());
    }


    public void handleTimeout(Long userId) {
        String battleKey = getUserBattle(userId);

        if (battleKey == null) return;
        Battle battle = getBattle(battleKey);

        boolean battleNull = battle == null;
        boolean organizer = battle.getOrganizerId() == userId && battle.getOrganizerScore() != null;
        boolean participant = battle.getParticipantId() == userId && battle.getParticipantScore() != null;
        if (!battleNull && !organizer && !participant) {
            writeRecord(battleKey, userId, -1);
        }

        redisTemplate.opsForHash().delete(USER_BATTLE_KEY, userId.toString());
    }
}
