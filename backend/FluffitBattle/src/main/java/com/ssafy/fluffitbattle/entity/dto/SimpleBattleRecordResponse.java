package com.ssafy.fluffitbattle.entity.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SimpleBattleRecordResponse {
    private List<BattleRecordItemDto> content;
    private boolean hasNext;

    @Builder
    public SimpleBattleRecordResponse(List<BattleRecordItemDto> content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
}