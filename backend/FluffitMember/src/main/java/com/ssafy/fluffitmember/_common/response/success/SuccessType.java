package com.ssafy.fluffitmember._common.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessType {

    ;
    private final HttpStatus status; //http status
    private final String msg; //success message

}
