package com.backend.oneqjob.companyUser.controller;

import com.backend.oneqjob.companyUser.dto.CompanySignUpRequest;
import com.backend.oneqjob.companyUser.exception.CustomException;
import com.backend.oneqjob.companyUser.exception.ErrorCode;
import com.backend.oneqjob.companyUser.service.BusinessNumberVerificationService;
import com.backend.oneqjob.companyUser.service.CompanyUserSignUpService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CompanyUserSignUpController {

    private final CompanyUserSignUpService signUpService;
    private final BusinessNumberVerificationService verificationService;
    private final HttpSession session;

    @PostMapping("/checkBusiness/{CompanyAccount}")
    public ResponseEntity<String> BusinessNumberVerification(@PathVariable String companyAccount, HttpSession session) {
        String result = verificationService.businessRegistration(companyAccount,session);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사업자 등록 정보를 조회하는 중 오류가 발생했습니다.");
        }
    }

    //회원가입
    @PostMapping("/company/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody CompanySignUpRequest request, BindingResult bindingResult) {

        Boolean emailVerified = (Boolean) session.getAttribute("emailVerified");
        if (emailVerified == null || !emailVerified) {
           throw new RuntimeException("Email verification required");
        }
        Boolean businessNumberVerified = (Boolean) session.getAttribute("businessNumberVerified");
        if (businessNumberVerified == null || !businessNumberVerified) {
            throw new RuntimeException("BusinessNumber verification required.");
        }
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("User format is invalid");
        }
        if (!request.getCompanyPw1().equals(request.getCompanyPw2())) {
            bindingResult.reject("passwordIncorrect");
        }

        try {
            signUpService.companySignup(request);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("Signup Error occurred");
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("Signup Error occurred");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("successfully sinUp");
    }
}
