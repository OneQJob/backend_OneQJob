package com.backend.oneqjob.domain.email.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

    private final EmailService emailService;

    public EmailServiceTest(@Autowired EmailService emailService) {
        this.emailService = emailService;
    }

    @Test
    void sendEmail() {
        String code = emailService.sendEmail("jshwang0901@naver.com");
        System.out.println("code = " + code);
    }
}