package com.backend.oneqjob.domain.email.controller;

import com.backend.oneqjob.domain.email.common.EmailConst;
import com.backend.oneqjob.domain.email.exception.CodeNotMatchException;
import com.backend.oneqjob.domain.email.service.EmailService;
import com.backend.oneqjob.entity.dto.EmailVerifyRequestDto;
import com.backend.oneqjob.global.api.ResponseDto;
import com.backend.oneqjob.global.util.EmailUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

//TODO Swagger, Validation
@Slf4j
@RestController
@RequestMapping("/verification/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    /**
     * 인증코드 전송
     * @param toEmail 사용자의 이메일
     * @param isSignUp 회원가입을 위한 요청이면 true, 그 외 기능(비밀번호 찾기 등)을 위한 요청이면 false
     * @return 전송 결과
     */
    @GetMapping
    public ResponseEntity<ResponseDto> sendEmailVerificationCode(
            @RequestParam("to-email") String toEmail,
            @RequestParam("signup") Boolean isSignUp,
            HttpServletRequest request
    ) {

        if (isSignUp) {
            //TODO 이미 해당 이메일을 사용하는 사업자가 있는지 확인 -> 사업자 회원가입 로직과 merge 한 후
        }

//        String verificationCode = emailService.sendEmail(toEmail);
//        -> 계정 막힘. 당분간 아래 방법으로 대체
        String verificationCode = EmailUtils.generateCode();

        HttpSession session = request.getSession();
        session.setAttribute(EmailConst.TRANSMISSION_SESSION, verificationCode);
        session.setMaxInactiveInterval(180);

        log.info("verification code={}", session.getAttribute(EmailConst.TRANSMISSION_SESSION));

        ResponseDto<Object> responseDto = new ResponseDto<>(true, "OTPCode was successfully sent", null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 인증코드 검증
     * @param requestDto 사용자의 이메일, 사용자가 입력한 인증코드
     * @param realCode 실제 메일로 전송된 인증코드 (세션에 저장된 인증코드)
     * @return 검증 결과
     */
    @PostMapping
    public ResponseEntity<ResponseDto> checkEmailVerificationCode(
            @Valid @RequestBody EmailVerifyRequestDto requestDto,
            @SessionAttribute(name = EmailConst.TRANSMISSION_SESSION, required = false) String realCode,
            HttpServletRequest request
    ) {
        if (realCode == null) {
            throw new SessionException("email verification code is not stored in session");
        }

        if (!realCode.equals(requestDto.getVerificationCode())) {
            throw new CodeNotMatchException("email verification code is not matched");
        }

        HttpSession session = request.getSession();
        session.setAttribute(EmailConst.VERIFICATION_SESSION, true);
        session.setMaxInactiveInterval(600);

        log.info("is-verification-success={}", session.getAttribute(EmailConst.VERIFICATION_SESSION));

        ResponseDto<Object> responseDto = new ResponseDto<>(true, "Authentication code verification succeeded.", null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
