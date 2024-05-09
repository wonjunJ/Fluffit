package com.ssafy.fluffitmember.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateNicknameReqDto {
    private String nickname;
}
