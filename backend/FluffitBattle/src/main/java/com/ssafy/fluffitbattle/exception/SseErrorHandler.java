//package com.ssafy.fluffitbattle.exception;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Component
//public class SseErrorHandler {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public void handleError(Throwable throwable, SseEmitter emitter) {
//        try {
//            if (throwable instanceof PetNotFoundException) {
//                PetNotFoundException ex = (PetNotFoundException) throwable;
//                String jsonResponse = objectMapper.writeValueAsString(new ErrorResponse(404, ex.getMessage()));
//                emitter.send(SseEmitter.event()
//                        .id(UUID.randomUUID().toString())
//                        .name("error")
//                        .data(jsonResponse));
//                emitter.complete();
//            } else if (throwable instanceof UserAlreadyInMatchingException) {
//                UserAlreadyInMatchingException ex = (UserAlreadyInMatchingException) throwable;
//                String jsonResponse = objectMapper.writeValueAsString(new ErrorResponse(409, ex.getMessage()));
//                emitter.send(SseEmitter.event()
//                        .id(UUID.randomUUID().toString())
//                        .name("error")
//                        .data(jsonResponse));
//                emitter.complete();
//            } else {
//                String jsonResponse = objectMapper.writeValueAsString(new ErrorResponse(500, "Unknown error occurred"));
//                emitter.send(SseEmitter.event()
//                        .id(UUID.randomUUID().toString())
//                        .name("error")
//                        .data(jsonResponse));
//                emitter.complete();
//            }
//        } catch (IOException e) {
//            emitter.completeWithError(e);
//        }
//    }
//}
