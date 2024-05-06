package com.ssafy.fluffitmember.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final TokenRepository tokenRepository;

    public void saveTokenInfo(String memberId, String refreshToken, String accessToken) {
        tokenRepository.save(new SavedToken(memberId, accessToken, refreshToken));
    }
}
