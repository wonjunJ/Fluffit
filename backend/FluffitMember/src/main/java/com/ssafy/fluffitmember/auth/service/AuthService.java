package com.ssafy.fluffitmember.auth.service;

import com.ssafy.fluffitmember.auth.dto.request.LoginReqDto;
import com.ssafy.fluffitmember.auth.dto.response.LoginResDto;
import com.ssafy.fluffitmember.exception.EncryptionException;
import com.ssafy.fluffitmember.jwt.GeneratedToken;
import com.ssafy.fluffitmember.jwt.JwtUtil;
import com.ssafy.fluffitmember.jwt.SavedToken;
import com.ssafy.fluffitmember.jwt.TokenRepository;
import com.ssafy.fluffitmember.member.entity.Member;
import com.ssafy.fluffitmember.member.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final Environment env;
    private final TokenRepository tokenRepository;

    @Transactional
    public LoginResDto login(LoginReqDto loginReqDto) throws NoSuchAlgorithmException, InvalidKeyException {

        String encryptedData = encrypt(loginReqDto.getUserCode());

        //암호화한 데이터가 다르면 에러 반환
        if(!encryptedData.equals(loginReqDto.getSignature())){
            throw new EncryptionException();
        }

        //암호화된 id를 이용해 memberTable에서 데이터를 찾음
        Optional<Member> findMember = memberRepository.findBySocialId(encryptedData);
        String  memberId = null;
        //만약 데이터가 없다면 새로 회원가입 진행
        if(findMember.isEmpty()) {
            memberId = UUID.randomUUID().toString();
            register(loginReqDto,memberId);
        }else{
            memberId = findMember.get().getMemberId();
        }

        //token 생성 redis 저장
        GeneratedToken generatedToken = jwtUtil.generateToken(memberId);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        LoginResDto loginResDto = mapper.map(generatedToken,LoginResDto.class);

        return loginResDto;
    }

    @Transactional
    public void register(LoginReqDto loginReqDto, String memberId) {
        Member member = Member.of(memberId,loginReqDto.getSignature(), loginReqDto.getUserCode(),0,0,0);
        Member savedMember = memberRepository.save(member);
    }

    public String encrypt(String data) throws NoSuchAlgorithmException, InvalidKeyException {

        // 시크릿 키로부터 SecretKeySpec 객체 생성
        String hmackey = env.getProperty("security.hmackey");
        log.info("////////hmackey = "+hmackey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(hmackey.getBytes(), "HmacSHA256");

        // HMAC SHA-256 Mac 인스턴스 생성
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);

        // 데이터를 암호화
        byte[] result = mac.doFinal(data.getBytes());

        String encodeData = Base64.getEncoder().encodeToString(result);
        log.info("encodeData = " + encodeData);
        // 바이너리 데이터를 Base64로 인코딩하여 문자열로 반환
        return encodeData;
    }

    public LoginResDto regenerateToken(String refreshToken) throws ExpiredJwtException, SignatureException {
        String memberId = jwtUtil.getUserId(refreshToken);

        Optional<SavedToken> findToken = tokenRepository.findById(memberId);

        if(findToken.isEmpty()){
            throw new ExpiredJwtException(null, null, "The token is expired or does not exist.");
        }

        GeneratedToken generatedToken = jwtUtil.generateToken(memberId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        LoginResDto loginResDto = mapper.map(generatedToken,LoginResDto.class);
        return loginResDto;
    }
}
