package com.ssafy.fluffitmember.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;

    @Builder
    private ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public static ErrorResponse from(ErrorStateCode errorStateCode) {
        return ErrorResponse.builder()
                .code(errorStateCode.getStatus())
                .message(errorStateCode.getMsg())
                .build();
    }
}
