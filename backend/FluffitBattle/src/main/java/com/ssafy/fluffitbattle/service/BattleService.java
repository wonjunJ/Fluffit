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
    private final RedisTemplate<String, String> redisTemplate;
    @Qualifier("battleRedisTemplate")
    private final RedisTemplate<String, Battle> battleRedisTemplate;

    @Qualifier("userBattleLongRedisTemplate")
    private final RedisTemplate<String, Long> userBattleLongRedisTemplate;
    @Qualifier("userBattleObjectRedisTemplate")
    private final RedisTemplate<String, String> userBattleObjectRedisTemplate;

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
        System.out.println("리퀘스트배틀 들어왔다 !!!!! {} "+ userId);

        userBattleObjectRedisTemplate.opsForHash().put(USER_BATTLE_KEY, "리퀘스트...", "들어가니");
        System.out.println(userBattleObjectRedisTemplate.opsForHash().get(USER_BATTLE_KEY, "리퀘스트..."));
        boolean success = false;

        int retryCount = 0;
        int maxRetries = 5;

        while (!success && retryCount < maxRetries) {
            retryCount++;
            AtomicBoolean shouldRetry = new AtomicBoolean(false);

            try {
                List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
                    @Override
                    public List<Object> execute(RedisOperations operations) throws DataAccessException {
                        operations.watch(BATTLE_QUEUE_KEY); // 대기 큐에
                        System.out.println("워치 설정은 성공한 듯");

                        ListOperations<String, String> listOps = operations.opsForList();
                        String opponentId = listOps.leftPop(BATTLE_QUEUE_KEY);
                        System.out.println("원래 큐에 있던 사람 " + (opponentId != null ? opponentId : "!!!없어요!!!"));

                        operations.multi(); // 레디스 트랜잭션 큐에 쌓기 시작
                        System.out.println("멀티 설정도 성공");

                        if (opponentId == null || getUserBattle(opponentId) != null) {
                            shouldRetry.set(true);
                            operations.opsForList().rightPush(BATTLE_QUEUE_KEY, userId);
                            operations.expire(BATTLE_QUEUE_KEY, 1, TimeUnit.MINUTES);
                            log.info(userId + " 배틀큐에 들어갔어요");
                            logCurrentQueueState(BATTLE_QUEUE_KEY); // Redis에 값이 정상적으로 추가되었는지 확인
                        } else if (Objects.equals(userId, opponentId) || getUserBattle(userId) != null) {
                            shouldRetry.set(false);
                        }
//                        else if (flupetFeignClient.getFlupetInfo(userId).getFlupetImageUrl() == null) {
//                            notificationService.notifyUser(userId, PET_DOES_NOT_EXIST_EVENTNAME, "");
//                        }
                        else {
                            userBattleObjectRedisTemplate.opsForHash().put(USER_BATTLE_KEY, "check", "che");
                            System.out.println("확인합니다 " + userBattleObjectRedisTemplate.opsForHash().get(USER_BATTLE_KEY, "check"));
                            shouldRetry.set(!createAndNotifyBattle(operations, userId, opponentId)); // setBattle 결과에 따라 재시도 설정

                            operations.opsForHash().put(USER_BATTLE_KEY, "안녕하세요", "Battle: 들어가는지 확인");
                        }

//                        operations.opsForHash().put(USER_BATTLE_KEY, userId, "Battle: 들어가는지 확인 밖");

                        return operations.exec(); // 트랜잭션 완료
                    }
                });

                System.out.println("execute 실행 됨?? " + (results.isEmpty() ? " 아니요" : results.get(0)));

                if (results == null || (results.isEmpty() && shouldRetry.get())) {
                    log.info("Transaction failed, retrying...");
                    System.out.println("트랜잭션 exec도 했는데 갑자기 실패함!!! 이거 뭐임!!!!!");
                    redisTemplate.unwatch(); // 트랜잭션 실패 시 unwatch 호출
                } else {
                    success = true; // 트랜잭션 성공 시 루프 종료
                }

            } catch (Exception e) {
                System.out.println("아 에러 제발");
                e.printStackTrace();
                redisTemplate.unwatch(); // 예외 발생 시 unwatch 호출
            }
        }

        log.info("리퀘스트 배틀 메서드가 끝날 때의 로그");
        logCurrentQueueState(BATTLE_QUEUE_KEY);
        System.out.println(redisTemplate.opsForHash().get(USER_BATTLE_KEY, userId));
    }

    private boolean createAndNotifyBattle(RedisOperations<String, String> operations, String userId, String opponentId) {
        log.info("살려줘!!!!!!!!!!!!!!!: {} vs {}", userId, opponentId);
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

        setUser(operations, userId, battleId);
        setUser(operations, opponentId, battleId);

        return setBattle(theBattle);
    }

    private void logCurrentQueueState(String key) { // 큐 상태 로깅
        ListOperations<String, String> listOps = redisTemplate.opsForList();
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
        Object result = userBattleLongRedisTemplate.opsForHash().get(USER_BATTLE_KEY, userId);
        return result != null ? "Battle:" + result.toString() : null;
    }

    private Battle getBattle(String battleKey) {
        Object result = battleRedisTemplate.opsForValue().get(battleKey);
        return result != null ? (Battle) result : null;
    }

    private void setUser(RedisOperations<String, String> operations, String userId, Long battleId) {
        try {
            userBattleLongRedisTemplate.opsForValue().set("User:" + userId, battleId, 80, TimeUnit.SECONDS);
            System.out.println(" 레디스에 들어가는 거 맞잖아 맞다고 해 " + userBattleLongRedisTemplate.opsForValue().get("User:" + userId));
            operations.opsForValue().set("User:" + userId, "Battle:" + battleId, 80, TimeUnit.SECONDS);
            System.out.println(" operations 들어가는 거 맞잖아 맞다고 해 " + operations.opsForValue().get("User:" + userId));

            System.out.println("Setting hash in Redis: " + USER_BATTLE_KEY + " -> " + userId + " : " + "Battle:" + battleId);
            userBattleObjectRedisTemplate.opsForHash().put(USER_BATTLE_KEY, userId, "Battle:" + battleId);
            System.out.println(" 레디스에 해시도 들어가야 하는데 " + userBattleObjectRedisTemplate.opsForHash().get(USER_BATTLE_KEY, userId));
            operations.opsForHash().put(USER_BATTLE_KEY, userId, "Battle:" + battleId);
            System.out.println(" 레디스에 해시도 들어가야 하는데 " + operations.opsForHash().get(USER_BATTLE_KEY, userId));
        } catch (Exception e) {
            System.err.println("Failed to set user in Redis: " + e.getMessage());
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
        redisTemplate.delete(battleKey);
        redisTemplate.delete("User:" + battle.getOrganizerId());
        redisTemplate.delete("User:" + battle.getParticipantId());
        redisTemplate.opsForHash().delete(USER_BATTLE_KEY, battle.getOrganizerId());
        redisTemplate.opsForHash().delete(USER_BATTLE_KEY, battle.getParticipantId());
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
        String battleKey = getUserBattle(userId);
        System.out.println("배틀 키 ~~~~~ " + battleKey);

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
