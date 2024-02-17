package com.backend.oneqjob.domain.user.controller;

import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.domain.user.service.OtpService;
import com.backend.oneqjob.entity.dto.VerificationCodeDto;
import com.backend.oneqjob.global.api.ResponseDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class SmsController {
    private final OtpService otpService;

    public SmsController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/checkOtp")
    public ResponseEntity<ResponseDto> sendOtp(@RequestBody VerificationCodeDto otpCodeRequest) {
        try {
            boolean isValid = otpService.verifyOtp(otpCodeRequest);
            if (isValid) {
                ResponseDto responseDto = new ResponseDto(true, "Authentication code verification succeeded.", null);
                return ResponseEntity.ok(responseDto);
            } else {
                ResponseDto responseDto = new ResponseDto(false, "Verification failed due to unexpected error.", null);
                return ResponseEntity.badRequest().body(responseDto);
            }
        } catch (CustomException e) {
            ResponseDto responseDto = new ResponseDto(false, e.getMessage(), null);
            return ResponseEntity.badRequest().body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(false, "An error occurred during verification.", null);
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
}

