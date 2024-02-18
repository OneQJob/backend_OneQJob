package com.backend.oneqjob.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailVerifyRequestDto {

    @NotNull
    private String email;

    @NotNull
    private String verificationCode;

}
