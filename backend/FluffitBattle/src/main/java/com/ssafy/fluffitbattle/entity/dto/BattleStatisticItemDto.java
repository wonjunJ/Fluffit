package com.ssafy.fluffitbattle.entity.dto;

import com.ssafy.fluffitbattle.entity.BattleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleStatisticItemDto {
    private String title;
    private long totalCount;
    private long winCount;
    private long loseCount;
    private double winRate;

    public void calculateWinRate() {
        if (totalCount > 0) {
            this.winRate = Math.round(((double) winCount / totalCount) * 1000) / 10.0;
        }
    }

    public BattleStatisticItemDto(BattleType battleType, long totalCount, long winCount, long loseCount) {
        this.title = battleType.getTitle();
        this.totalCount = totalCount;
        this.winCount = winCount;
        this.loseCount = loseCount;
        calculateWinRate();
    }
}