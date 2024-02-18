package com.backend.oneqjob.domain.user.exception;


public class CustomException extends RuntimeException {
    private final int code;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}





