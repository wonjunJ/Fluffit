package com.ssafy.fluffitmember.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankDto {
    private int rank;
    private String userNickname;
    private int battlePoint;
    private String flupetNickname;
    private List<String> flupetImageUrl;
}
