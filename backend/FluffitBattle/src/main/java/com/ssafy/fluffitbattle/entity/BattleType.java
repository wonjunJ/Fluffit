package com.ssafy.fluffitbattle.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

@Getter
@RequiredArgsConstructor
@JsonSerialize(using = BattleTypeSerializer.class)
@JsonDeserialize(using = BattleTypeDeserializer.class)
public enum BattleType {

    ROCK("BATTLE_ROCK", "돌 깨기", 60, "1분 내에 더 많은 돌을 터치해서 깨자!"),
    HEARTRATE("BATTLE_HEARTRATE", "심박수", 60, "1분 내에 더 높은 심박 수를 기록하자!")
//    ,    STEPS("STEPS", "걸음 수", 1, "1분 내에 더 많은 걸음 수를 기록하자!")
    ;

    private final String key;
    private final String title;
    private final int time; // 초
    private final String description;
}