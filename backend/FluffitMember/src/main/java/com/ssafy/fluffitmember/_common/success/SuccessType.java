package com.ssafy.fluffitmember._common.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessType {


    //****************************User****************************//
    UPDATE_USER_NICKNAME_SUCCESSFULLY(200,"닉네임 수정 성공"),
    LOGIN_SUCCESSFULLY(200,"로그인 성공"),
    SIGNOUT_SUCCESSFULLY(200,"회원탈퇴 성공"),

    ;
    private final int status;
    private final String msg; //success message

}
