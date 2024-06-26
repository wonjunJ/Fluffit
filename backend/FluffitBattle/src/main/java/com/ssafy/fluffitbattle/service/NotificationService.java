package com.ssafy.fluffitbattle.service;

import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.entity.dto.BattleMatchingResponseDto;
//import com.ssafy.fluffitbattle.exception.SseErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
//    private final SseErrorHandler sseErrorHandler;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Qualifier("stringRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;
    private static final String BATTLE_QUEUE_KEY = "battle_queue";
//    private final Sinks.Many<ServerSentEvent<Battle>> sink = Sinks.many().multicast().onBackpressureBuffer();
//
//    public Flux<ServerSentEvent<Battle>> getBattleNotifications() {
//        return sink.asFlux();
//    }

    public SseEmitter createEmitter(String userId, String whichController) {
        SseEmitter emitter = new SseEmitter(6000L * 5); // 5분 정도 연결
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> {
            handleEmitterCompletion(userId, whichController);
            emitters.remove(userId);
        });

        emitter.onError(ex -> {
            handleEmitterError(userId, whichController, ex);
            emitters.remove(userId);
        });
        return emitter;
    }

//    public void removeUserEmitter(Long userId) {
//        emitters.remove(userId);
//    }
//
//    public SseEmitter existUserEmitter(Long userId) {
//        return emitters.get(userId);
//    }

    public void ridOfUserFromWaitingQueue(String userId) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        String waitUser = listOps.leftPop(BATTLE_QUEUE_KEY);
        if (waitUser != null && !waitUser.equals(userId)) {
            listOps.rightPush(BATTLE_QUEUE_KEY, userId);
        }
    }

//    public SseEmitter createBattleEmitter(Long userId) {
//        SseEmitter emitter = new SseEmitter(60000L * 2); // 1분 지나면 타임 아웃
//        emitters.put(userId, emitter);
//        emitter.onCompletion(() -> emitters.remove(userId));
//        emitter.onTimeout(() -> emitters.remove(userId));
//        return emitter;
//    }

    public void notifyUser(String userId, String eventName, Object message) {
        SseEmitter emitter = emitters.get(userId);
//        if (emitter == null) {
//            createEmitter(userId);
//            emitter = emitters.get(userId);
//        }
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(message));
                emitter.complete();
            } catch (IOException e) {
                emitters.remove(userId); // 오류 발생 시 연결 제거
            }
        }
    }

    private void handleEmitterCompletion(String userId, String whichController) {
        if (whichController.equals("wait") && redisTemplate.opsForList().leftPop(BATTLE_QUEUE_KEY).equals(userId)) {
            notifyUser(userId, "fail_matching", BattleMatchingResponseDto.builder().result(false).build());
        }
        ridOfUserFromWaitingQueue(userId);
    }

    private void handleEmitterError(String userId, String whichController, Throwable ex) {
//        sseErrorHandler.handleError(ex, emitters.get(userId));
        if (whichController.equals("wait")) {
            notifyUser(userId, "fail_matching", BattleMatchingResponseDto.builder().result(false).build());
        }
        log.info("emitter error : {}", ex);
        ridOfUserFromWaitingQueue(userId);
    }

//    public void notifyUsers(Battle battle) {
//        emitters.forEach((userId, emitter) -> {
//            try {
//                emitter.send(SseEmitter.event().name("battle-match").data(battle));
//            } catch (IOException e) {
//                emitters.remove(userId); // 오류 발생 시 연결 제거
//            }
//        });
////        ServerSentEvent<Battle> event = ServerSentEvent.<Battle>builder()
////                .event("battle-match")
////                .data(battle)
////                .build();
////        sink.tryEmitNext(event);
//    }

//    public void notifyMatching(Battle battle) {
//        Long[] userIds = {battle.getOrganizerId(), battle.getParticipantId()};
//        for (Long userId : userIds) {
//            SseEmitter emitter = emitters.get(userId);
//            if (emitter != null) {
//                try {
//                    emitter.send(SseEmitter.event().name("battle-match").data(battle));
//                } catch (IOException e) {
//                    emitters.remove(userId); // 오류 발생 시 연결 제거
//                }
//            }
//        }
//    }

}

