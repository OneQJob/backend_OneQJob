package com.backend.oneqjob.domain.user.exception;

public enum ErrorCode {
    USER_NOT_FOUND(100, "User not found"),
    DATABASE_ERROR(102, "Database error occurred"),
    SIGNUP_ERROR(103, "Signup Error occurred"),
    AUTH_ERROR(104, "Auth Error occurred"),
    PASSWORD_FORMAT_INVALID(105, "Password format is invalid"),
    USER_ID_DUPLICATE(106, "UserId already exists"),
    USER_ID_FORMAT_INVALID(107, "UserId format is invalid"),
    SMS_SENDING_ERROR(108, "Error occurred while sending SMS"),
    SESSION_ERROR(109, "Session handling error"),
    INVALID_CODE(110, "Invalid verification code"),
    TIMEOUT(111, "Session timeout or OTP expired");

    private final int code;
    private final String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}