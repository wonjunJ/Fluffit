package com.ssafy.fluffitbattle.controller;

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
@RequestMapping("/api/battle")
public class BattleController {

    private final BattleService battleService;
    private final NotificationService notificationService;

    // 매칭 요청 처리
    @PostMapping("/regist")
    public ResponseEntity<SseEmitter> requestBattle(@RequestParam Long userId) {
        SseEmitter sseEmitter = notificationService.createEmitter(userId);
        battleService.requestBattle(userId);
        return ResponseEntity.ok(sseEmitter);
    }

    // 게임 레디
    @PostMapping("/ready")
    public ResponseEntity<SseEmitter> readyBattle(@RequestParam Long userId) {
        battleService.confirmBattle(userId);
        return ResponseEntity.ok().build();
    }

    // 배틀 결과 처리
    @PostMapping("/finish")
    public ResponseEntity<Void> finishBattle(@RequestParam Long battleId, @RequestParam Long record) {
        battleService.submitBattleRecord(battleId, record);
        return ResponseEntity.ok().build();
    }
}
