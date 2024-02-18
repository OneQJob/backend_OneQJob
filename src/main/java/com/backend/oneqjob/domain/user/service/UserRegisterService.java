package com.backend.oneqjob.domain.user.service;

import com.backend.oneqjob.entity.dto.SignUpRequestDto;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;

import java.util.Map;

public interface UserRegisterService {
    Map<String, Object> signUpRequired(SignUpRequestDto requestDto) throws Exception;

}
