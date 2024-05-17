package com.ssafy.fluffitbattle.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
public class CustomExceptionHandler {

    private final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public void addEmitter(String userId, SseEmitter emitter) {
        sseEmitters.put(userId, emitter);
    }

    public void removeEmitter(String userId) {
        sseEmitters.remove(userId);
    }

    @ExceptionHandler(PetNotFoundException.class)
    public void handlePetNotFoundException(PetNotFoundException ex) {
        String userId = extractUserIdFromMessage(ex.getMessage());
        SseEmitter emitter = sseEmitters.get(userId);

        if (emitter != null) {
            try {
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .errorCode(ex.getErrorCode())
                        .errorMessage(ex.getMessage())
                        .build();

                emitter.send(SseEmitter.event().name("error").data(errorResponse));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }

    private String extractUserIdFromMessage(String message) {
        // 예: "userId님, 펫이 없어요!" 메시지에서 userId를 추출하는 로직
        return message.split("님")[0];
    }

}
