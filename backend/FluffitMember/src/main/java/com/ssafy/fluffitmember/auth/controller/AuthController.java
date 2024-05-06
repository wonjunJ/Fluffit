package com.ssafy.fluffitmember.auth.controller;


import com.ssafy.fluffitmember.auth.dto.request.LoginReqDto;
import com.ssafy.fluffitmember.auth.dto.response.LoginResDto;
import com.ssafy.fluffitmember.auth.service.AuthService;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.ssafy.fluffitmember.error.ErrorResponse;
import com.ssafy.fluffitmember.error.ErrorStateCode;
import com.ssafy.fluffitmember.exception.EncryptionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> login(@RequestBody LoginReqDto loginReqDto) {
        LoginResDto loginResDto = null;
        try {
            loginResDto = authService.login(loginReqDto);
            return ResponseEntity.ok().body(loginResDto);
        } catch (NoSuchAlgorithmException e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorStateCode.NO_SUCH_ALGORITHM));
        } catch (InvalidKeyException e) {
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorStateCode.INVALID_HASH_KEY));
        } catch (EncryptionException e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorStateCode.ENCRYPTION_MISMATCH));
        }
    }

}
