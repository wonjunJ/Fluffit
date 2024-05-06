package com.ssafy.fluffitmember._common.exception;

import com.ssafy.fluffitmember._common.response.error.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomBadRequestException extends RuntimeException {

    private final ErrorType errorType;

}
