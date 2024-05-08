package com.ssafy.fluffitbattle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/battle/stream")
public class NotificationController {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitters.remove(emitter);
        });

        return emitter;
    }

    public void dispatch(String message) {
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("message").data(message));
            } catch (IOException e) {
                emitter.completeWithError(e);
                this.emitters.remove(emitter);
            }
        });
    }
}
