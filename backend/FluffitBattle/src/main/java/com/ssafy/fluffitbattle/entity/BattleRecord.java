package com.ssafy.fluffitbattle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleRecord {
    private Long userId;
    private Integer score;
}