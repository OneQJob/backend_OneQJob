package com.backend.oneqjob.domain.user.service;

import com.backend.oneqjob.entity.dto.SmsRequestDto;
import com.backend.oneqjob.entity.dto.VerificationCodeDto;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;

import java.util.Map;

public interface OtpService {

    String createSession(SmsRequestDto requestDto) throws Exception;

    SingleMessageSendingRequest beforeRequest(String sessionId) throws Exception;

    Map<String, Object> getData(String sessionId) throws Exception;

    boolean verifyOtp (VerificationCodeDto codeDto);



}
