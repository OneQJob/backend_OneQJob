package com.backend.oneqjob.entity.dto;

import lombok.Data;

@Data
public class EmailVerifyRequestDto {

    private String email;
    private String verificationCode;

}
