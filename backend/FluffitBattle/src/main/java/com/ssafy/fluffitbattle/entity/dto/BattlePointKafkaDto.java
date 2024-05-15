package com.ssafy.fluffitbattle.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BattlePointKafkaDto {
    private String memberId;
    private int point;
}
