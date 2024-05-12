package com.ssafy.fluffitmember._common.success;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse <T> {

    private final int status;
    private final String msg; //response message
    private final T data; //response data

    @Builder
    private SuccessResponse(int status,String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    public static SuccessResponse<Void> from(SuccessType successType) {
        return SuccessResponse.<Void>builder()
                .status(successType.getStatus())
                .msg(successType.getMsg())
                .build();
    }
    public static <T> SuccessResponse<T> of(SuccessType successType, T data) {
        return SuccessResponse.<T>builder()
                .status(successType.getStatus())
                .msg(successType.getMsg())
                .data(data)
                .build();
    }
}
