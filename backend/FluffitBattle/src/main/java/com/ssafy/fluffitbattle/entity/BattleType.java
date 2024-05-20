package com.ssafy.fluffitbattle.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BattleType {

    ROCK("BATTLE_ROCK", "돌 깨기"),
    HEARTRATE("BATTLE_HEARTRATE", "심박수"),
    STEPS("STEPS", "걸음 수");

    private final String key;
    private final String title;
}
