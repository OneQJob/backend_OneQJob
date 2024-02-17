package com.backend.oneqjob.domain.user.service;

import com.backend.oneqjob.entity.dto.SmsRequestDto;
import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;

import java.util.Map;

public interface SendOtpService {

    HttpSession createSession(HttpSession session, SmsRequestDto requestDto) throws Exception;

    SingleMessageSendingRequest beforeRequest(HttpSession session) throws Exception;

    Map<String, Object> getData(HttpSession session) throws Exception;


}
