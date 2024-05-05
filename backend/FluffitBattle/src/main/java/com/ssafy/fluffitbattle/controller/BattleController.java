package com.ssafy.fluffitbattle.controller;

import com.ssafy.fluffitbattle.service.BattleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/battle")
public class BattleController {

    private final BattleService battleService;
}
