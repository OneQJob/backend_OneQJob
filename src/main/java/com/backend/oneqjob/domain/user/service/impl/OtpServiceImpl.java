package com.backend.oneqjob.domain.user.service.impl;

import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.domain.user.exception.ErrorCode;
import com.backend.oneqjob.domain.user.service.OtpService;
import com.backend.oneqjob.entity.dto.VerificationCodeDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;




@Service
public class OtpServiceImpl implements OtpService {


//    private static final Logger LOGGER = Logger.getLogger(OtpServiceImpl.class.getName());

//    public boolean verifyOtp(VerificationCodeDto codeDto,String sessionId) {
//
//        String phone = codeDto.getPhoneNumber();
//        String code = codeDto.getCode();
//
//        // 세션 속성 로깅
//        public boolean verifyOtp(VerificationCodeDto codeDto, String sessionId) {
//            // 세션 ID로 세션 조회
//            Session session = sessionRepository.findById(sessionId);
//            if (session == null) {
//                LOGGER.warning("Session not found. Throwing TIMEOUT exception.");
//                throw new CustomException(ErrorCode.TIMEOUT);
//            }
//
//            String phone = codeDto.getPhoneNumber();
//            String code = codeDto.getCode();
//
//            String sessionPhoneNumber = session.getAttribute("phoneNumber");
//            String sessionCode = session.getAttribute("code");
//
//            LOGGER.info("Attempting to verify OTP. Session ID: " + sessionId);
//            LOGGER.info("Session Phone Number: " + sessionPhoneNumber + ", Provided Phone Number: " + phone);
//            LOGGER.info("Session Code: " + sessionCode + ", Provided Code: " + code);
//
//            if (sessionPhoneNumber == null || sessionCode == null) {
//                LOGGER.warning("Session attributes are null. Throwing TIMEOUT exception.");
//                throw new CustomException(ErrorCode.TIMEOUT);
//            }
//
//            if (!phone.equals(sessionPhoneNumber) || !code.equals(sessionCode)) {
//                LOGGER.warning("Provided phone number or code does not match session. Throwing INVALID_CODE exception.");
//                throw new CustomException(ErrorCode.INVALID_CODE);
//            }
//
//            // 세션 상태 업데이트
//            session.setAttribute("validStatus", true);
//            sessionRepository.save(session); // 변경된 세션 저장
//            LOGGER.info("OTP verification successful. Session validStatus set to true.");
//            return true;
//        }
//    }




    public boolean verifyOtp(VerificationCodeDto codeDto, String sessionId) {
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
        return true;
    }
}


