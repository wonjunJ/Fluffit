package com.ssafy.fluffitbattle.exception;

public class PetNotFoundException extends RuntimeException {
    private final int errorCode;

    public PetNotFoundException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
