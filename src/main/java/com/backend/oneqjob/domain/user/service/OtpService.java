package com.backend.oneqjob.domain.user.service;

import com.backend.oneqjob.entity.dto.VerificationCodeDto;
import jakarta.servlet.http.HttpSession;

public interface OtpService {
    boolean verifyOtp (VerificationCodeDto codeDto, HttpSession session);
}
