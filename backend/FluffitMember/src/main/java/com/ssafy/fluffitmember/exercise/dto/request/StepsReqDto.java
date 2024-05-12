package com.ssafy.fluffitmember.exercise.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StepsReqDto {
    private LocalDate date;
    private int stepCount;
}
