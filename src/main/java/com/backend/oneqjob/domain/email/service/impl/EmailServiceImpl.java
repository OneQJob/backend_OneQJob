package com.backend.oneqjob.domain.email.service.impl;

import com.backend.oneqjob.domain.email.service.EmailService;
import com.backend.oneqjob.global.util.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.oneqjob.domain.email.common.EmailConst.SUBJECT;
import static com.backend.oneqjob.domain.email.common.EmailConst.TEXT_TEMPLATE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    //TODO @Async 확인
    @Override
    public String sendEmail(String to) {
        String verificationCode = EmailUtils.generateCode();

        SimpleMailMessage email = createEmailForm(to, verificationCode);

        emailSender.send(email); //TODO MailException 예외 처리
        return verificationCode;
    }

    private SimpleMailMessage createEmailForm(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(SUBJECT);
        message.setText(TEXT_TEMPLATE + verificationCode);

        return message;
    }

}
