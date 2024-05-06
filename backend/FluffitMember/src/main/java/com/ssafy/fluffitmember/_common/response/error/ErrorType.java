package com.ssafy.fluffitmember._common.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    ;
    private final HttpStatus status; //http status
    private final String msg; //error message
}
