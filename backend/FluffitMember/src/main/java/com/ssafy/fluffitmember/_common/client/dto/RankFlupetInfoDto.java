package com.ssafy.fluffitmember._common.client.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RankFlupetInfoDto {
    private String flupetNickname;
    private List<String> flupetImageUrl;
}