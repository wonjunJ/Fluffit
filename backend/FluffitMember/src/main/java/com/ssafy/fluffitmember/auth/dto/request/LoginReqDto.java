package com.ssafy.fluffitmember.auth.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginReqDto {

    private final String userCode; // 사용자의 naver id
    private final String signature; // 암호화된 id

    @Builder
    private LoginReqDto(String userCode,String signature){
        this.userCode = userCode;
        this.signature = signature;
    }

    public static LoginReqDto of(String userCode,String signature){
        return builder()
                .userCode(userCode)
                .signature(signature)
                .build();
    }   
}
