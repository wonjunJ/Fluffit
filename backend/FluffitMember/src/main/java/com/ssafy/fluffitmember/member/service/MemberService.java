package com.ssafy.fluffitmember.member.service;

import com.ssafy.fluffitmember._common.client.FlupetClient;
import com.ssafy.fluffitmember._common.client.dto.RankFlupetInfoDto;
import com.ssafy.fluffitmember._common.exception.DuplicateNickname;
import com.ssafy.fluffitmember._common.exception.EncryptionException;
import com.ssafy.fluffitmember._common.exception.NotFoundUserException;
import com.ssafy.fluffitmember._common.exception.NotValidNickname;
import com.ssafy.fluffitmember._common.exception.NotValidSQL;
import com.ssafy.fluffitmember.member.dto.request.SignOutResDto;
import com.ssafy.fluffitmember.member.dto.request.UpdateNicknameReqDto;
import com.ssafy.fluffitmember.member.dto.response.*;
import com.ssafy.fluffitmember.member.entity.Member;
import com.ssafy.fluffitmember.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final FlupetClient flupetClient;

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

    public GetPointResDto getUserPoint(String memberId) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        GetPointResDto getPointResDto = new GetPointResDto();
        getPointResDto.setPoint(findMember.map(Member::getBattlePoint).orElse(-1));
        return getPointResDto;
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

        Member myMember = findMember.get();
        // 요청한 멤버아이디의 랭크값을 얻어옴
        Optional<Integer> memberRankById = memberRepository.findRankByMemberId(myMember.getMemberId());
        if(memberRankById.isEmpty()){
            throw new NotFoundUserException();
        }
        log.info("memberId = " +memberId + " // myRankId = ",memberRankById.get());

        RankDto myRank = new RankDto();
        myRank.setRank(memberRankById.get());
        myRank.setUserNickname(myMember.getNickname());
        myRank.setBattlePoint(myMember.getBattlePoint());

        List<Member> rankerList = memberRepository.findTop3ByBattlePoint();
        if(rankerList.isEmpty()){
            throw new NotValidSQL();
        }
        List<Integer> rankerRank = memberRepository.findTop3RankingsByBattlePoint();
        if(rankerRank.isEmpty()){
            throw new NotValidSQL();
        }

        // 회원 ID 목록 수집 및 findMember의 ID 추가
        List<String> memberIds = rankerList.stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());
        findMember.ifPresent(member -> memberIds.add(member.getMemberId()));

        //feign client로 flupet 관련 정보 얻기
        List<RankFlupetInfoDto> flupetRanks = flupetClient.getFlupetRanks(memberIds);

        List<RankDto> rankerInfo = new ArrayList<>();
        for (int i = 0; i < rankerList.size(); i++) {
            Member member = rankerList.get(i);
            Integer rank = rankerRank.get(i);
            RankFlupetInfoDto rankFlupetInfoDto = flupetRanks.get(i);  // 순서를 맞추기 위해 동일 인덱스 사용

            rankerInfo.add(new RankDto(rank, member.getNickname(),member.getBattlePoint(), rankFlupetInfoDto.getFlupetNickname(), rankFlupetInfoDto.getFlupetImageUrl()));
        }

        RankFlupetInfoDto myflupet = flupetRanks.get(flupetRanks.size()-1);
        myRank.setFlupetNickname(myflupet.getFlupetNickname());
        myRank.setFlupetImageUrl(myflupet.getFlupetImageUrl());

        GetRankResDto getRankResDto = new GetRankResDto();
        getRankResDto.setRanking(rankerInfo);
        getRankResDto.setMyRank(myRank);

        return getRankResDto;
    }

    public void signOut(String memberId, SignOutResDto signOutResDto) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        if(findMember.isEmpty()){
            throw new NotFoundUserException();
        }
        Member member = findMember.get();

        if(!member.getSocialId().equals(signOutResDto.getSignature())){
            throw new EncryptionException();
        }
    }


}
