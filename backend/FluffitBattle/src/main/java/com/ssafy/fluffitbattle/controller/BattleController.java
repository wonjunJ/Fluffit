package com.ssafy.fluffitbattle.controller;

import com.ssafy.fluffitbattle.entity.dto.BattleResultRequestDto;
import com.ssafy.fluffitbattle.service.BattleService;
import com.ssafy.fluffitbattle.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class BattleController {

    private final BattleService battleService;
    private final NotificationService notificationService;

    // 매칭 요청 처리
    @PostMapping("/wait")
    public ResponseEntity<SseEmitter> requestBattle(@RequestHeader("memberId") String memberId) {
//        Long userId = Long.parseLong(memberId);
        SseEmitter sseEmitter = notificationService.createEmitter(memberId);
        battleService.requestBattle(memberId);
        return ResponseEntity.ok(sseEmitter);
    }

    // 대기 취소
    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelWaiting(@RequestHeader("memberId") String memberId) {
//        Long userId = Long.parseLong(memberId);
        notificationService.ridOfUserFromWaitingQueue(memberId);
        return ResponseEntity.ok().build();
    }

//    // 게임 레디
//    @PostMapping("/ready")
//    public ResponseEntity<SseEmitter> readyBattle(@RequestParam Long userId) {
//        battleService.confirmBattle(userId);
//        return ResponseEntity.ok().build();
//    }

    // 배틀 결과 처리
    @PostMapping("/result")
    public ResponseEntity<SseEmitter> finishBattle(@RequestHeader("memberId") String memberId, @RequestBody BattleResultRequestDto battleResultRequestDto) {
//        Long userId = Long.parseLong(memberId);
        SseEmitter sseEmitter = notificationService.createEmitter(memberId);
        battleService.submitBattleRecord(memberId, battleResultRequestDto);
        return ResponseEntity.ok(sseEmitter);
    }
}
