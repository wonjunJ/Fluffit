package com.ssafy.fluffitbattle.service;

import com.ssafy.fluffitbattle.entity.Battle;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
//    private final Sinks.Many<ServerSentEvent<Battle>> sink = Sinks.many().multicast().onBackpressureBuffer();
//
//    public Flux<ServerSentEvent<Battle>> getBattleNotifications() {
//        return sink.asFlux();
//    }

    public SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(600000L); // 10분 지나면 타임 아웃
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        return emitter;
    }

    public void notifyUser(Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("battleEvent").data(message));
            } catch (IOException e) {
                emitters.remove(userId); // 오류 발생 시 연결 제거
            }
        }
    }

    public void notifyUsers(Battle battle) {
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("battle-match").data(battle));
            } catch (IOException e) {
                emitters.remove(userId); // 오류 발생 시 연결 제거
            }
        });
//        ServerSentEvent<Battle> event = ServerSentEvent.<Battle>builder()
//                .event("battle-match")
//                .data(battle)
//                .build();
//        sink.tryEmitNext(event);
    }

    public void notifyMatching(Battle battle) {
        Long[] userIds = {battle.getOrganizerId(), battle.getParticipantId()};
        for (Long userId : userIds) {
            SseEmitter emitter = emitters.get(userId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event().name("battle-match").data(battle));
                } catch (IOException e) {
                    emitters.remove(userId); // 오류 발생 시 연결 제거
                }
            }
        }
    }

}

