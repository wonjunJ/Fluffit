package com.ssafy.fluffitbattle.service;

import com.ssafy.fluffitbattle.client.FlupetFeignClient;
import com.ssafy.fluffitbattle.client.MemberFeignClient;
import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.entity.BattleType;
import com.ssafy.fluffitbattle.entity.dto.*;
import com.ssafy.fluffitbattle.exception.ErrorResponse;
import com.ssafy.fluffitbattle.exception.PetNotFoundException;
import com.ssafy.fluffitbattle.exception.UserAlreadyInMatchingException;
import com.ssafy.fluffitbattle.kafka.KafkaProducer;
import com.ssafy.fluffitbattle.repository.BattleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Collectors;

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

    @PersistenceContext
    private final EntityManager entityManager;

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

        // /* 강제 매칭용 임시 코드
        if (stringRedisTemplate.opsForList().size(BATTLE_QUEUE_KEY) == 0) {
            stringRedisTemplate.opsForList().rightPush(BATTLE_QUEUE_KEY, "20a88ab0-ee97-4cac-ab6f-c7f94ac9b5cf");
        }
        //

        if (flupetFeignClient.getFlupetInfo(userId).getFlupetImageUrl() == null) {
            ErrorResponse errorResponse = new ErrorResponse(404, "펫이 없어요!");
            notificationService.notifyUser(userId, "error", errorResponse);
//            throw new PetNotFoundException(404, userId + "님, 펫이 없어요!");
            return;
        }

        if (getUserBattle(userId) != null) {
            ErrorResponse errorResponse = new ErrorResponse(409, "이미 배틀에 참가 중!");
            notificationService.notifyUser(userId, "error", errorResponse);
//            throw new UserAlreadyInMatchingException(409, userId + "님, 이미 배틀에 참가 중!");
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
                            if (!userId.equals(opponentId)) operations.expire(BATTLE_QUEUE_KEY, 1, TimeUnit.MINUTES);
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
            /* TODO
                원래 타임아웃 80초!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
             */
            userBattleLongRedisTemplate.opsForValue().set("User:" + userId, battleId, 32, TimeUnit.SECONDS);
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
        int maxRetries = 5;
        int retries = 0;
        boolean success = false;
        AtomicBoolean canGotoCalculate = new AtomicBoolean(false);

        while (retries < maxRetries && !success) {
            retries++;
            success = Boolean.TRUE.equals(battleRedisTemplate.execute(new SessionCallback<Boolean>() {
                @Override
                public Boolean execute(RedisOperations operations) throws DataAccessException {
                    operations.watch(battleKey);

                    Battle battle = (Battle) operations.opsForValue().get(battleKey);
                    if (battle == null) {
                        log.error("Battle not found for key: " + battleKey);
                        return false;
                    }

                    log.info("writeRecord 메서드 진입 " + battle.getId());

                    AtomicBoolean isOpponentSubmitted = new AtomicBoolean(false);

                    if (userId.equals(battle.getOrganizerId())) {
                        battle.setOrganizerScore(score);
                        isOpponentSubmitted.set(battle.getParticipantScore() != null);
                    } else {
                        battle.setParticipantScore(score);
                        isOpponentSubmitted.set(battle.getOrganizerScore() != null);
                    }

                    operations.multi();
                    operations.opsForValue().set(battleKey, battle);
                    List<Object> results = operations.exec();

                    if (results == null || results.isEmpty()) {
                        // 트랜잭션 실패
                        log.info("Transaction failed, retrying...");
                        return false;
                    } else {
                        // 트랜잭션 성공
                        if (isOpponentSubmitted.get()) {
                            canGotoCalculate.set(true);
//                            calculateWinnerAndNotifyResult(battleKey);
                        }
                        return true;
                    }
                }
            }));

            if (!success) {
                try {
                    Thread.sleep(100); // 재시도 간격 설정
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrupted during retry sleep", e);
                }
            }
        }

        if (!success) {
            log.error("Failed to write record after " + maxRetries + " attempts");
        } else if (canGotoCalculate.get()) {
            calculateWinnerAndNotifyResult(battleKey);
        }
    }

    public void submitBattleRecord(String userId, BattleResultRequestDto requestDto) {
        String battleKey = "Battle:" + requestDto.getBattleId();
        writeRecord(battleKey, userId, requestDto.getScore());
    }

    private void calculateWinnerAndNotifyResult(String battleKey) {
        Battle battle = getBattle(battleKey);

        if (battle == null) return;  // Null 체크 추가
        log.info("calculate winner 메서드 진입 " + battle.getId());

        // 승자 결정 및 카프카 메시지 생성
        battle = determineWinnerAndUpdatePoints(battle);

        // 배틀 정보 업데이트 및 저장
        updateAndSaveBattle(battle);

        // Redis와 관련된 클린업 작업
        cleanUpRedisEntries(battle, battleKey);

        // 결과 통지
        notifyResults(battle);
    }

    private Battle determineWinnerAndUpdatePoints(Battle battle) {
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

        log.info("determineWinner 메서드 진입 " + battle.getId());

        kafkaProducer.send("battle-point-update", organizerKafkaDto);
        kafkaProducer.send("battle-point-update", participantKafkaDto);

        return battle;
    }

    @Transactional//(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void updateAndSaveBattle(Battle battle) {
        battle.setBattleDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        log.info("배틀 날짜 " + battle.getBattleDate());
        log.info("배틀 상태 " + battle.toString());

        battleRedisTemplate.opsForValue().set("Battle:" + battle.getId(), battle);

////        try {
////            battleRepository.save(battle);
//////            entityManager.setFlushMode(FlushModeType.AUTO);
////            entityManager.flush();
////        } catch (Exception e) {
////            log.error("Error during save: ", e);
//////            e.printStackTrace();
////        }
////
//        log.info("Original Saved Battle: {}", battleRepository.findById(battle.getId()).orElse(null));
////
////        battle = entityManager.merge(battle);
////        log.info("Merged Battle: {}", battleRepository.findById(battle.getId()).orElse(null));
//
//        try {
//            entityManager.setFlushMode(FlushModeType.AUTO);
//            battleRepository.saveAndFlush(battle);
//            entityManager.persist(battle);
//            entityManager.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Battle theBattle = battleRepository.findById(battle.getId()).get();
        theBattle.setBattleDate(battle.getBattleDate());
        theBattle.setOrganizerScore(battle.getOrganizerScore());
        theBattle.setParticipantScore(battle.getParticipantScore());
        theBattle.setWinnerId(battle.getWinnerId());
        battleRepository.saveAndFlush(theBattle);


////        log.info(battleRepository.save(battle).toString());
//        log.info("save " + battleRepository.findById(battle.getId()).orElse(null));
        battle = entityManager.merge(theBattle);
        entityManager.flush();
        log.info("merge " + battleRepository.findById(battle.getId()).orElse(null));
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
        log.info("만료 유저의 배틀 " + battleKey);

        if (battleKey == null) return;
        Battle battle = getBattle(battleKey);

        boolean battleNull = battle == null;
        boolean organizer = Objects.equals(battle.getOrganizerId(), userId) && battle.getOrganizerScore() != null;
        boolean participant = Objects.equals(battle.getParticipantId(), userId) && battle.getParticipantScore() != null;

        log.info(battleNull + " " + organizer + " " + participant);
        if (!battleNull && !organizer && !participant) {
            writeRecord(battleKey, userId, -1);
        }

        objectRedisTemplate.opsForHash().delete(USER_BATTLE_KEY, userId);
    }

    public SimpleBattleRecordResponse getBattleRecords(String userId, Pageable pageable) {
        Slice<Battle> slice = battleRepository.findByOrganizerIdOrParticipantIdOrderByBattleDateDesc(userId, userId, pageable);

        List<BattleRecordItemDto> simpleContent = slice.getContent().stream()
                .map(battle -> convertToDto(battle, userId))
                .collect(Collectors.toList());

        return new SimpleBattleRecordResponse(simpleContent, slice.hasNext());
    }

//    public Slice<BattleRecordItemDto> getBattleRecords(String userId, Pageable pageable) {
//        return battleRepository.findByOrganizerIdOrParticipantIdOrderByBattleDateDesc(userId, userId, pageable)
//                .map(battle -> convertToDto(battle, userId));
//    }

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
