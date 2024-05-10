package com.ssafy.fluffitmember.member.service;

import com.ssafy.fluffitmember.exception.DuplicateNickname;
import com.ssafy.fluffitmember.exception.NotFoundUserException;
import com.ssafy.fluffitmember.exception.NotValidNickname;
import com.ssafy.fluffitmember.member.dto.Request.UpdateNicknameReqDto;
import com.ssafy.fluffitmember.member.dto.Response.GetNicknameResDto;
import com.ssafy.fluffitmember.member.entity.Member;
import com.ssafy.fluffitmember.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateNickname(String memberId, UpdateNicknameReqDto updateNicknameReqDto) {

        Optional<Member> member = memberRepository.findByMemberId(memberId);
        if(member.isEmpty()){
            throw new NotFoundUserException();
        }
        validNickname(updateNicknameReqDto.getNickname());
        member.get().updateNickname(updateNicknameReqDto.getNickname());
    }

    private void validNickname(String nickname) {

        if(!nickname.matches("^[a-zA-Z0-9가-힣]{1,8}$")){
            throw new NotValidNickname();
        }
        if(memberRepository.findByNickname(nickname).isPresent()){
            throw new DuplicateNickname();
        }
    }

    public int getUserCoin(String memberId) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        return findMember.map(Member::getCoin).orElse(-1);
    }

    public GetNicknameResDto getNickname(String memberId) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        if (findMember.isEmpty()){
            throw new NotFoundUserException();
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        log.info("before mapper nickname " + findMember.get().getNickname());
        GetNicknameResDto getNicknameResDto = mapper.map(findMember.get().getNickname(), GetNicknameResDto.class);
        log.info("after mapper nickname " + getNicknameResDto.getNickname());
        return getNicknameResDto;
    }
}
