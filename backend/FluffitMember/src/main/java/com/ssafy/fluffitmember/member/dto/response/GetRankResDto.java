package com.ssafy.fluffitmember.member.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetRankResDto {
    private List<RankDto> ranking;
    private RankDto myRank;
}
