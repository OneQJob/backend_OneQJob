package com.backend.oneqjob.companyUser.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySignUpRequest {
    @NotBlank
    private String companyName;

    @NotBlank
    private String companyAccount;

    private String companyLogoImgPileName;

    private String companyLogoImgUrl;

    @Email
    @NotBlank
    private String companyEmail;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String companyPw1;

    @NotBlank
    private String companyPw2;
}
