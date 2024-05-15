package com.ssafy.fluffitbattle.entity.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BattleResultResponseDto {
    private Boolean isWin;
    private Integer opponentBattleScore;
    private Integer myBattleScore;
    private Long battlePoint;
    private Integer battlePointChanges;
}