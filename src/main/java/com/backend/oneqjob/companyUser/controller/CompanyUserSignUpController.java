package com.backend.oneqjob.companyUser.controller;

import com.backend.oneqjob.companyUser.dto.BusinessNumberRequest;
import com.backend.oneqjob.companyUser.dto.CompanySignUpRequest;
import com.backend.oneqjob.companyUser.service.BusinessNumberCheckService;
import com.backend.oneqjob.companyUser.service.CompanySignUpServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompanyUserSignUpController {

    private final CompanySignUpServiceImpl signUpService;
    private final BusinessNumberCheckService checkService;

    /**
     * @param request (사업자 회원정보)
     * @param bindingResult (회원 정보 검증 form bindingResult)
     * @return (binding 실패 시 에러 메세지, 회원가입 시 성공 메세지)
     */
    @PostMapping("/company/signup")
    public ResponseEntity signup(@Valid @RequestBody CompanySignUpRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input information.");
        }
        if (!request.getCompanyPw1().equals(request.getCompanyPw2())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }
        try {
            signUpService.companySignup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("successfully singUp");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("Signup Error occurred");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate input information.");
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("Signup Error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed: Internal server error");
        }
    }


    //사업자조회 컨트롤러
    @PostMapping("/company/BusinessNumber")
    public ResponseEntity<String> BusinessNumberVerification(@RequestBody BusinessNumberRequest businessNumber) {

        try {
            checkService.check(businessNumber);
            return ResponseEntity.ok("Business number verified.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error retrieving business registration number.");
        }
    }
}
