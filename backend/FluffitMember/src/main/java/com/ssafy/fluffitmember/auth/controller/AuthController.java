package com.ssafy.fluffitmember.auth.controller;


import com.ssafy.fluffitmember.auth.dto.request.LoginReqDto;
import com.ssafy.fluffitmember.auth.dto.response.LoginResDto;
import com.ssafy.fluffitmember.auth.service.AuthService;
import com.ssafy.fluffitmember._common.exception.NotValidRefreshToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.ssafy.fluffitmember._common.error.ErrorResponse;
import com.ssafy.fluffitmember._common.error.ErrorType;
import com.ssafy.fluffitmember._common.exception.EncryptionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginReqDto loginReqDto) {
        LoginResDto loginResDto = null;
        try {
            loginResDto = authService.login(loginReqDto);
            log.info("getSignature() = " + loginReqDto.getSignature());
            log.info("getUserCode() = " + loginReqDto.getUserCode());
            return ResponseEntity.ok().body(loginResDto);
        } catch (NoSuchAlgorithmException e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorType.NO_SUCH_ALGORITHM));
        } catch (InvalidKeyException e) {
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorType.INVALID_HASH_KEY));
        } catch (EncryptionException e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorType.ENCRYPTION_MISMATCH));
        }
    }

    @PostMapping("/regenerate-token")
    public ResponseEntity<Object> regenerateToken(@RequestHeader("Authorization") final String refreshToken) {
        LoginResDto loginResDto = null;
        try {
            loginResDto = authService.regenerateToken(refreshToken);
            return ResponseEntity.ok().body(loginResDto);
        }catch (ExpiredJwtException e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorType.REFRESH_TOKEN_EXPIRE));
        }catch (SignatureException e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorType.TOKEN_INVALID));
        }catch (NotValidRefreshToken e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorType.NOT_VALID_REFRESHTOKEN));
        }
    }
}
