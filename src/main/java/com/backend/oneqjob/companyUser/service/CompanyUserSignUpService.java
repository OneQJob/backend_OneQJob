package com.backend.oneqjob.companyUser.service;

import com.backend.oneqjob.companyUser.repository.CompanyRepository;
import com.backend.oneqjob.companyUser.dto.CompanySignUpRequest;
import com.backend.oneqjob.entity.CompanyUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyUserSignUpService {

    private final CompanyRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    //회원가입
    public void companySignup(CompanySignUpRequest request) {
        CompanyUser companyUser = CompanyUser.builder()
                .companyName(request.getCompanyName())
                .companyAccount(request.getCompanyAccount())
                .companyLogoImgUrl(request.getCompanyLogoImgUrl())
                .companyLogoImgPileName(request.getCompanyLogoImgPileName())
                .companyEmail(request.getCompanyEmail())
                .companyPw(passwordEncoder.encode(request.getCompanyPw1()))
                .build();
        this.repository.save(companyUser);
    }
}

