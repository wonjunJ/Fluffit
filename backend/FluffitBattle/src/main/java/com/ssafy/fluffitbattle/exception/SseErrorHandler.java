package com.ssafy.fluffitbattle.exception;

import com.ssafy.fluffitbattle.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SseErrorHandler {

    private final NotificationService notificationService;

    public void handleError(Throwable throwable, SseEmitter emitter) {
        try {
            if (throwable instanceof PetNotFoundException) {
                PetNotFoundException ex = (PetNotFoundException) throwable;
                emitter.send(SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .name("error")
                        .data(new ErrorResponse(404, ex.getMessage())));
                emitter.complete();
            } else if (throwable instanceof UserAlreadyInMatchingException) {
                UserAlreadyInMatchingException ex = (UserAlreadyInMatchingException) throwable;
                emitter.send(SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .name("error")
                        .data(new ErrorResponse(409, ex.getMessage())));
                emitter.complete();
            } else {
                // 다른 예외 처리
                emitter.send(SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .name("error")
                        .data(new ErrorResponse(500, "Unknown error occurred")));
                emitter.complete();
            }
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }
}
