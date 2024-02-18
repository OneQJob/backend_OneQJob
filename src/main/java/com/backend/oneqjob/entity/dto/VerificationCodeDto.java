package com.backend.oneqjob.entity.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationCodeDto {

    private String phoneNumber;
    private String code;
    private String sessionId;

}