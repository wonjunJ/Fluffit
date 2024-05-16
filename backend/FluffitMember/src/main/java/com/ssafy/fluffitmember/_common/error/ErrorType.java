package com.ssafy.fluffitmember._common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    ACCESS_TOKEN_EXPIRE(403, "ACCESS 토큰 만료"),
    REDIS(408, "캐시 서버에 문제가 있습니다"),
    RUNTIME(500, "서버 에러"),
    REFRESH_TOKEN_EXPIRE(401, "리프레시 토큰 만료"),
    TOKEN_INVALID(400, "토큰 형식이 잘못되었습니다"),
    TOKEN_UNAUTH(402, "토큰이 일치하지 않습니다"),
    NOT_VALID_REFRESHTOKEN(400, "저장된 리프레시 토큰과 다릅니다"),
    NO_SUCH_ALGORITHM(4001,"요청된 암호화 알고리즘이 시스템에서 지원되지 않거나 존재하지 않습니다."),
    INVALID_HASH_KEY(4002,"제공된 키가 암호화 알고리즘에 부적합하거나 규격에 맞지 않습니다"),
    ENCRYPTION_MISMATCH(4003,"암호화된 데이터가 서로 다릅니다"),
    NOT_FOUND_MEMBER(400,"조회된 사용자가 없습니다"),
    NOT_VALID_NICKNAME(401,"유효한 닉네임이 아닙니다"),
    DUPLICATE_NICKNAME(402,"중복된 닉네임입니다"),
    NOT_VALID_SQL(500,"SQL 오류 입니다")
    ;
    private final int status;
    private final String msg;

}
