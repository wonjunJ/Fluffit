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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
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
    private final FlupetFeignClient flupetFeignClient;
    private final KafkaProducer kafkaProducer;

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
    public void requestBattle(String userId) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        String opponentId = listOps.leftPop(BATTLE_QUEUE_KEY);

        if (opponentId == null) {
            listOps.rightPush(BATTLE_QUEUE_KEY, userId);
            redisTemplate.expire(BATTLE_QUEUE_KEY, 1, TimeUnit.MINUTES);

            // Redis에 값이 정상적으로 추가되었는지 확인
            Long queueSize = listOps.size(BATTLE_QUEUE_KEY);  // 큐의 현재 크기를 가져옵니다.
            List<String> queueContents = listOps.range(BATTLE_QUEUE_KEY, 0, queueSize);  // 큐의 전체 내용을 가져옵니다.
            log.info("Current queue size after adding {}: {}", userId, queueSize);
            log.info("Queue contents: {}", queueContents);

//            notificationService.notifyUser(userId, WAIT_MATCHING_EVENTNAME,
//                    BattleMatchingResponseDto.builder().result(false).build());
        } else if (Objects.equals(userId, opponentId)) {
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

            log.info("살려줘!!!!!!!!!!!!! " + userId + " " + opponentId);

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

            setBattle(theBattle);
        }
    }

    private void notifyJustMatched(String userId, String opponentId, Battle battle) {
        FlupetInfoTempClientDto opponentFlupetInfoDto = flupetFeignClient.getFlupetInfo(opponentId);
        String imgUrl;
        if (opponentFlupetInfoDto.getImageUrl() != null && !opponentFlupetInfoDto.getImageUrl().isEmpty()) {
            imgUrl = opponentFlupetInfoDto.getImageUrl().get(opponentFlupetInfoDto.getImageUrl().size() - 1);
        } else {
            imgUrl = "";
        }
        notificationService.notifyUser(userId, JUST_NOW_MATCHED_EVENTNAME,
                BattleMatchingResponseDto.builder()
                        .result(true)
                        .opponentName(memberFeignClient.getNickName(opponentId).getNickname())
                        .opponentBattlePoint(memberFeignClient.getBattlePoint(opponentId).getPoint())
                        .opponentFlupetName(opponentFlupetInfoDto.getFlupetName())
                        .opponentFlupetImageUrl(imgUrl)
                        .battleId(battle.getId())
                        .battleType(battle.getBattleType())
                        .build());
    }

    private String getUserBattle(String userId) {
        Object result = redisTemplate.opsForHash().get(USER_BATTLE_KEY, userId);
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

    private void setUser(String userId, Long battleId) {
        redisTemplate.opsForValue().set("User:" + userId, "Battle:" + battleId, 80, TimeUnit.SECONDS);
        redisTemplate.opsForHash().put(USER_BATTLE_KEY, userId, "Battle:" + battleId);
    }

    private void setBattle(Battle battle) {
        battleRedisTemplate.opsForValue().set("Battle:" + battle.getId(), battle, 1, TimeUnit.HOURS);
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

        String organizerId = battle.getOrganizerId();
        String participantId = battle.getParticipantId();

        BattlePointKafkaDto organizerKafkaDto;
        BattlePointKafkaDto participantKafkaDto;
        if (battle.getOrganizerScore() > battle.getParticipantScore()) {
            battle.setWinnerId(organizerId);
            organizerKafkaDto = new BattlePointKafkaDto(organizerId, 10);
            participantKafkaDto = new BattlePointKafkaDto(participantId, -5);
        } else if (battle.getOrganizerScore() < battle.getParticipantScore()) {
            battle.setWinnerId(participantId);
            organizerKafkaDto = new BattlePointKafkaDto(organizerId, -5);
            participantKafkaDto = new BattlePointKafkaDto(participantId, 10);
        } else {
            organizerKafkaDto = new BattlePointKafkaDto(organizerId, -5);
            participantKafkaDto = new BattlePointKafkaDto(participantId, -5);
        }

        kafkaProducer.send("battle-point-update", organizerKafkaDto);
        kafkaProducer.send("battle-point-update", participantKafkaDto);

        battle.setBattleDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        battleRepository.save(battle);
        redisTemplate.delete(battleKey);

        redisTemplate.delete("User:" + organizerId);
        redisTemplate.delete("User:" + participantId);

        redisTemplate.opsForHash().delete(USER_BATTLE_KEY, organizerId);
        redisTemplate.opsForHash().delete(USER_BATTLE_KEY, participantId);

        notifyBattleResult(organizerId, battle);
        notifyBattleResult(participantId, battle);
    }

    private void notifyBattleResult(String userId, Battle battle) {
        boolean isWin = Objects.equals(battle.getWinnerId(), userId);
        notificationService.notifyUser(userId, BATTLE_RESULT_EVENTNAME,
                BattleResultResponseDto.builder()
                .isWin(isWin)
                .battlePoint(Long.valueOf(memberFeignClient.getBattlePoint(userId).getPoint()))
                .battlePointChanges(isWin ? 10 : -5)
                .myBattleScore(Objects.equals(battle.getOrganizerId(), userId) ? battle.getOrganizerScore() : battle.getParticipantScore())
                .opponentBattleScore(Objects.equals(battle.getOrganizerId(), userId) ? battle.getParticipantScore() : battle.getOrganizerScore())
                .build());
    }


    public void handleTimeout(String userId) {
        String battleKey = getUserBattle(userId);

        if (battleKey == null) return;
        Battle battle = getBattle(battleKey);

        boolean battleNull = battle == null;
        boolean organizer = Objects.equals(battle.getOrganizerId(), userId) && battle.getOrganizerScore() != null;
        boolean participant = Objects.equals(battle.getParticipantId(), userId) && battle.getParticipantScore() != null;
        if (!battleNull && !organizer && !participant) {
            writeRecord(battleKey, userId, -1);
        }

        redisTemplate.opsForHash().delete(USER_BATTLE_KEY, userId);
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
