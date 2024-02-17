package com.backend.oneqjob.domain.user.service.impl;

import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.domain.user.exception.ErrorCode;
import com.backend.oneqjob.domain.user.service.OtpService;
import com.backend.oneqjob.entity.dto.VerificationCodeDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpService {
    public boolean verifyOtp(VerificationCodeDto codeDto, HttpSession session) {
        String phone = codeDto.getPhoneNumber();
        String code = codeDto.getVerificationCode();

        String sessionPhoneNumber = (String) session.getAttribute("phoneNumber");
        String sessionCode = (String) session.getAttribute("code");


        if (sessionPhoneNumber == null || sessionCode == null) {
            throw new CustomException(ErrorCode.TIMEOUT);
        }


        if (!phone.equals(sessionPhoneNumber) || !code.equals(sessionCode)) {
            throw new CustomException(ErrorCode.INVALID_CODE);
        }

        session.setAttribute("validStatus", true);
        session.setMaxInactiveInterval(600);
        return true;
    }
}


