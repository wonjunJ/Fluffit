package com.ssafy.fluffitmember.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginReqDto {

    private String userCode; // 사용자의 naver id
    private String signature; // 암호화된 id
}
