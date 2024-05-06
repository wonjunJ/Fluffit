package com.ssafy.fluffitmember.auth.service;

import com.ssafy.fluffitmember._common.exception.CustomBadRequestException;
import com.ssafy.fluffitmember._common.response.error.ErrorType;
import com.ssafy.fluffitmember._common.response.success.SuccessResponse;
import com.ssafy.fluffitmember.auth.dto.request.LoginReqDto;
import com.ssafy.fluffitmember.auth.dto.response.LoginResDto;
import com.ssafy.fluffitmember.member.repository.MemberRepository;
import com.ssafy.fluffitmember.redis.TokenRepository;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    @Value("${security.hmacKey}")
    private static String secretKey;


    public LoginResDto login(LoginReqDto loginReqDto) throws NoSuchAlgorithmException, InvalidKeyException {

        String encryptedData = encrypt(loginReqDto.getUserCode());

        if(!encryptedData.equals(loginReqDto.getSignature())){
            throw new CustomBadRequestException(ErrorType.NOT_AUTHENTICATED_USER);
        }




    }

    public static String encrypt(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        // 시크릿 키로부터 SecretKeySpec 객체 생성
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");

        // HMAC SHA-256 Mac 인스턴스 생성
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);

        // 데이터를 암호화
        byte[] result = mac.doFinal(data.getBytes());

        // 바이너리 데이터를 Base64로 인코딩하여 문자열로 반환
        return Base64.getEncoder().encodeToString(result);
    }

}
