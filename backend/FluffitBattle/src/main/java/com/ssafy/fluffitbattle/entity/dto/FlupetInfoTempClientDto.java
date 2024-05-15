package com.ssafy.fluffitbattle.entity.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FlupetInfoTempClientDto {
    private int fullness;
    private int health;
    private String flupetName;
    private List<String> imageUrl;
    private String birthDay;
    private String age;
    private boolean isEvolutionAvailable;
    private long nextFullnessUpdateTime;
    private long nextHealthUpdateTime;
    private int coin;
}
