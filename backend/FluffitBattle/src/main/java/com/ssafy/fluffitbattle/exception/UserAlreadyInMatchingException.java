package com.ssafy.fluffitbattle.exception;

public class UserAlreadyInMatchingException extends RuntimeException {
    private final int errorCode;

    public UserAlreadyInMatchingException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
