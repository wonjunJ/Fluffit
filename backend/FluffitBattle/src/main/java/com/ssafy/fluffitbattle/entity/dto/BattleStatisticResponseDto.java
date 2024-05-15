package com.ssafy.fluffitbattle.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleStatisticResponseDto {
    private Long battlePoint;
    private List<BattleStatisticItemDto> battleStatisticItemDtoList;
}
