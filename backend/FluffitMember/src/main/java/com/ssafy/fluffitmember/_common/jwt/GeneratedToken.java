package com.ssafy.fluffitmember._common.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder @ToString
public class GeneratedToken {

    private String accessToken;
    private String refreshToken;
}
