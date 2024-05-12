package com.ssafy.fluffitmember.member.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankDto {
    private int rank;
    private String nickname;
    private String flupetNickname;
    private String flupetImageUrl;
}
