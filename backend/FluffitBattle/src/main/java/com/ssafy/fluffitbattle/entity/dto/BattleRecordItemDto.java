package com.ssafy.fluffitbattle.entity.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BattleRecordItemDto {
    private boolean isWin;
    private String title;
    private String opponentName;
    private Integer opponentScore;
    private Integer myScore;
    private LocalDateTime date;
}
