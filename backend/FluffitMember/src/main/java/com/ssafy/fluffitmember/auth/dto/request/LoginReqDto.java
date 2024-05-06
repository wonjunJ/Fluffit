package com.ssafy.fluffitmember.auth.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginReqDto {
    private final String signature;

    @Builder
    private LoginReqDto(String signature){
        this.signature = signature;
    }

    public static LoginReqDto of(String signature){
        return builder()
                .signature(signature)
                .build();
    }
}
