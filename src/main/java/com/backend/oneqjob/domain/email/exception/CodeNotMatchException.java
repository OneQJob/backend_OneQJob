package com.backend.oneqjob.domain.email.exception;

public class CodeNotMatchException extends RuntimeException{

    public CodeNotMatchException(String message) {
        super(message);
    }
}
