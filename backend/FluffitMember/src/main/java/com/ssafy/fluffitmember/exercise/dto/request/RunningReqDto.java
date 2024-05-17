package com.ssafy.fluffitmember.exercise.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RunningReqDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int calorie;
}
