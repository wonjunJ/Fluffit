package com.ssafy.fluffitbattle.entity.dto;

import com.ssafy.fluffitbattle.entity.BattleType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BattleMatchingResponseDto {
    private Boolean result;
    private String opponentName;
    private String opponentFlupetName;
    private String opponentFlupetImageUrl;
    private String opponentBattlePoint;
    private Long battleId;
    private BattleType battleType;
}
