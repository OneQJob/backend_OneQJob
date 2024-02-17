package com.backend.oneqjob.domain.user.service.impl;

import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.domain.user.exception.ErrorCode;
import com.backend.oneqjob.domain.user.service.OtpService;
import com.backend.oneqjob.entity.dto.VerificationCodeDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.logging.Logger;


@Service
public class OtpServiceImpl implements OtpService {


    private static final Logger LOGGER = Logger.getLogger(OtpServiceImpl.class.getName());

    public boolean verifyOtp(VerificationCodeDto codeDto) {
        HttpSession session = getCurrentSession();
        String phone = codeDto.getPhoneNumber();
        String code = codeDto.getCode();

        // 세션 속성 로깅
        if (session.getAttributeNames() != null) {
            Collections.list(session.getAttributeNames()).forEach(attr ->
                    LOGGER.info("Session attribute - Key: " + attr + ", Value: " + session.getAttribute(attr)));
        } else {
            LOGGER.info("No session attributes found.");
        }

        // 기존 로깅
        LOGGER.info("Attempting to verify OTP. Session ID: " + session.getId());


        String sessionPhoneNumber = (String) session.getAttribute("phoneNumber");
        String sessionCode = (String) session.getAttribute("code");

        LOGGER.info("Session Phone Number: " + sessionPhoneNumber + ", Provided Phone Number: " + phone);
        LOGGER.info("Session Code: " + sessionCode + ", Provided Code: " + code);

        if (sessionPhoneNumber == null || sessionCode == null) {
            LOGGER.warning("Session attributes are null. Throwing TIMEOUT exception.");
            throw new CustomException(ErrorCode.TIMEOUT);
        }

        if (!phone.equals(sessionPhoneNumber) || !code.equals(sessionCode)) {
            LOGGER.warning("Provided phone number or code does not match session. Throwing INVALID_CODE exception.");
            throw new CustomException(ErrorCode.INVALID_CODE);
        }

        session.setAttribute("validStatus", true);
        session.setMaxInactiveInterval(600);
        LOGGER.info("OTP verification successful. Session validStatus set to true.");
        return true;
    }
    private HttpSession getCurrentSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attr != null ? attr.getRequest().getSession(false) : null;
    }

}

//    public boolean verifyOtp(VerificationCodeDto codeDto, HttpSession session) {
//        String phone = codeDto.getPhoneNumber();
//        String code = codeDto.getCode();
//
//        String sessionPhoneNumber = (String) session.getAttribute("phoneNumber");
//        String sessionCode = (String) session.getAttribute("code");
//
//
//        if (sessionPhoneNumber == null || sessionCode == null) {
//            throw new CustomException(ErrorCode.TIMEOUT);
//        }
//
//
//        if (!phone.equals(sessionPhoneNumber) || !code.equals(sessionCode)) {
//            throw new CustomException(ErrorCode.INVALID_CODE);
//        }
//
//        session.setAttribute("validStatus", true);
//        session.setMaxInactiveInterval(600);
//        return true;
//    }
//}


