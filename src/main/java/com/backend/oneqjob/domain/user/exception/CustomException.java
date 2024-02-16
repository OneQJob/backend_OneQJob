package com.backend.oneqjob.domain.user.exception;


public class CustomException extends RuntimeException {
    private final int code; // 에러 코드를 저장할 필드

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDescription()); // 상위 클래스인 RuntimeException의 message 필드에 에러 메시지 저장
        this.code = errorCode.getCode(); // 에러 코드 저장
    }

    public int getCode() {
        return code; // 에러 코드 반환
    }
}





