package com.ssafy.fluffitmember.member.dto.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetRankResDto {
    private List<RankDto> ranker;
    private RankDto myRank;
}
