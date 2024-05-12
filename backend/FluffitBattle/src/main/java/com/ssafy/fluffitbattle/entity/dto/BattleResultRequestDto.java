package com.ssafy.fluffitbattle.entity.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BattleResultRequestDto {
    private Long battleId;
    private Integer score;
}
