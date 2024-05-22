package com.ssafy.fluffitmember.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StepsKafkaDto {
    private String memberId;
    private int steps;
}
