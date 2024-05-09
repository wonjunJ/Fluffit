package com.ssafy.fluffitmember.auth.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResDto {

    private String accessToken;
    private String refreshToken;
}
