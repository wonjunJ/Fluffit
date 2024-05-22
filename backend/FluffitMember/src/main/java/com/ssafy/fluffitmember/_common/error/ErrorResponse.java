package com.ssafy.fluffitmember._common.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int status;
    private final String msg;

    @Builder
    private ErrorResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    public static ErrorResponse from(ErrorType errorType) {
        return ErrorResponse.builder()
                .status(errorType.getStatus())
                .msg(errorType.getMsg())
                .build();
    }
}
