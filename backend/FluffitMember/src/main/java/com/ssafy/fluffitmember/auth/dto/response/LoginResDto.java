package com.ssafy.fluffitmember.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResDto {

    private final String accessToken;
    private final String refreshToken;

    @Builder
    private LoginResDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResDto of(String accessToken, String refreshToken){
        return builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
