package com.ssafy.fluffitflupet.error

import org.springframework.http.HttpStatus

enum class ErrorType(
    val status: HttpStatus, //http status
    val msg: String //error message
) {
    TOO_SHORT_NICKNAME(HttpStatus.FORBIDDEN, "닉네임은 1자 이상이 되어야 합니다."),
    TOO_LONG_NICKNAME(HttpStatus.FORBIDDEN, "닉네임은 8자 이하가 되어야 합니다."),
    WRONG_CONDITION(HttpStatus.FORBIDDEN, "닉네임에는 영어, 한글, 숫자 이외의 문자를 허용하지 않습니다."),
    INVALID_USERID(HttpStatus.NOT_FOUND, "해당하는 유저의 플러펫을 찾을 수 없습니다."),
    INVALID_FOODID(HttpStatus.NOT_FOUND, "해당하는 음식을 찾을 수 없습니다."),
    NOT_AVAILABLE_EVOLVE(HttpStatus.FORBIDDEN, "더 이상 진화할 수 없습니다."),
    NOT_AVAILABLE_GEN_PET(HttpStatus.FORBIDDEN, "이미 플러펫이 존재하여 더 이상 플러펫을 생성할 수 없습니다."),
    NOT_FOUND_NEXT_FLUPET(HttpStatus.NOT_FOUND, "그 단계의 플러펫을 찾을 수 없습니다."),
    TEST_EXCEPTION(HttpStatus.BAD_REQUEST, "테스트 오류가 발생했어요"),
    PAT_TIME_LIMIT(HttpStatus.PAYMENT_REQUIRED, "쓰다듬기는 5분에 한번씩만 할 수 있어요"),
    PAT_CNT_LIMIT(HttpStatus.CONFLICT, "하루에 5번만 쓰다듬기를 할 수 있어요"),
    LOCK_NOT_AVAILABLE(HttpStatus.LOCKED, "Lock을 획득하지 못했습니다."),
    LOCK_INTERRUPTED_ERROR(HttpStatus.LOCKED, "Lock획득 대기 상태 중 인터럽트가 발생하였습니다."),
    INSUFFICIENT_COIN(HttpStatus.FORBIDDEN, "간식을 구매할 코인이 부족합니다."),
    NOT_REQUIRED_FULLNESS(HttpStatus.NOT_ACCEPTABLE, "이미 포만감이 가득찬 상태입니다."),
    INSUFFICIENT_FOOD_STOCK(HttpStatus.CONFLICT, "해당 음식의 재고가 부족합니다.")
    ;
}
