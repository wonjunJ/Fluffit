package com.ssafy.fluffitbattle.controller;

import com.ssafy.fluffitbattle.service.BattleService;
import com.ssafy.fluffitbattle.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/battle")
public class BattleController {

    private final BattleService battleService;
    private final NotificationService notificationService;

    // 매칭 요청 처리
    @PostMapping("/request")
    public ResponseEntity<SseEmitter> requestBattle(@RequestParam Long userId) {
        battleService.requestBattle(userId);
        return ResponseEntity.ok(notificationService.createEmitter(userId));
    }

    // 배틀 결과 처리
    @PostMapping("/finish")
    public ResponseEntity<Void> finishBattle(@RequestParam Long battleId, @RequestParam String result) {
        battleService.finishBattle(battleId, result);
        return ResponseEntity.ok().build();
    }
}
