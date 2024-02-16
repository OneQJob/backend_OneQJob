package com.backend.oneqjob.domain.user.exception;

public enum ErrorCode {
    USER_NOT_FOUND(100, "User not found"),
    DATABASE_ERROR(102, "Database error occurred"),
    SIGNUP_ERROR(103, "Signup Error occurred"),
    AUTH_ERROR(104, "Auth Error occurred"),
    PASSWORD_FORMAT_INVALID(105, "Password format is invalid"),
    USER_ID_DUPLICATE(106, "UserId already exists"),
    USER_ID_FORMAT_INVALID(107, "UserId format is invalid");

    private final int code; // 에러 코드
    private final String description; // 에러 메시지

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code; // 에러 코드 반환
    }

    public String getDescription() {
        return description; // 에러 메시지 반환
    }
}
