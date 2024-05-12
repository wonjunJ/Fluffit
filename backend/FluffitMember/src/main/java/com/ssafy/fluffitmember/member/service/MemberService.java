package com.ssafy.fluffitmember.member.service;

import com.ssafy.fluffitmember._common.exception.DuplicateNickname;
import com.ssafy.fluffitmember._common.exception.NotFoundUserException;
import com.ssafy.fluffitmember._common.exception.NotValidNickname;
import com.ssafy.fluffitmember._common.exception.NotValidSQL;
import com.ssafy.fluffitmember.member.dto.Request.UpdateNicknameReqDto;
import com.ssafy.fluffitmember.member.dto.Response.GetCoinResDto;
import com.ssafy.fluffitmember.member.dto.Response.GetNicknameResDto;
import com.ssafy.fluffitmember.member.dto.Response.GetRankResDto;
import com.ssafy.fluffitmember.member.dto.Response.RankDto;
import com.ssafy.fluffitmember.member.entity.Member;
import com.ssafy.fluffitmember.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
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

    public GetCoinResDto getUserCoin(String memberId) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);

        GetCoinResDto getCoinResDto = new GetCoinResDto();
        getCoinResDto.setCoin(findMember.map(Member::getCoin).orElse(-1));
        return getCoinResDto;
    }

    public GetNicknameResDto getNickname(String memberId) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        if (findMember.isEmpty()){
            throw new NotFoundUserException();
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        log.info("before mapper nickname " + findMember.get().getNickname());
        GetNicknameResDto getNicknameResDto = mapper.map(findMember.get(), GetNicknameResDto.class);
        log.info("after mapper nickname " + getNicknameResDto.getNickname());
        return getNicknameResDto;
    }

    public GetRankResDto getRank(String memberId) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        if (findMember.isEmpty()){
            throw new NotFoundUserException();
        }

        // 요청한 멤버아이디의 랭크값을 얻어옴
        Optional<Integer> memberRankById = memberRepository.findRankByMemberId(memberId);
        if(memberRankById.isEmpty()){
            throw new NotFoundUserException();
        }
        log.info("memberId = " +memberId + " // myRankId = ",memberRankById.get());

        RankDto myRank = new RankDto();
        myRank.setRank(memberRankById.get());
        myRank.setNickname(findMember.get().getNickname());

        List<Member> rankerList = memberRepository.findTop3ByBattlePoint();
        if(rankerList.isEmpty()){
            throw new NotValidSQL();
        }
        List<Integer> rankerRank = memberRepository.findTop3RankingsByBattlePoint();
        if(rankerRank.isEmpty()){
            throw new NotValidSQL();
        }

        //feign client로 flupet 관련 정보 얻기

        List<RankDto> rankerInfo = new ArrayList<>();

        
        // 1,2,3 위의 랭크값을 얻어옴 (점수가 같을 수 있음)


        return null;
    }
}
