package com.ssafy.fluffitmember.auth.controller;

import com.ssafy.fluffitmember._common.response.success.SuccessResponse;
import com.ssafy.fluffitmember.auth.dto.request.LoginReqDto;
import com.ssafy.fluffitmember.auth.dto.response.LoginResDto;
import com.ssafy.fluffitmember.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public SuccessResponse<LoginResDto> login(@RequestBody LoginReqDto loginReqDto){
        a
    }

}
