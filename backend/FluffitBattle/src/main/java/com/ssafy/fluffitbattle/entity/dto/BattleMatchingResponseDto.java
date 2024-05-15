package com.ssafy.fluffitbattle.entity.dto;

import com.ssafy.fluffitbattle.entity.BattleType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BattleMatchingResponseDto {
    private Boolean result;
    @Builder.Default
    private String opponentName = "";
    @Builder.Default
    private String opponentFlupetName = "";
    @Builder.Default
    private String opponentFlupetImageUrl = "";
    @Builder.Default
    private Integer opponentBattlePoint = 0;
    @Builder.Default
    private Long battleId = 0L;
    @Builder.Default
    private BattleType battleType = BattleType.ROCK;
}
