package com.backend.oneqjob.domain.user.service.impl;

import com.backend.oneqjob.domain.shared.redis.repository.RedisSessionRepository;
import com.backend.oneqjob.entity.dto.SmsRequestDto;
import com.backend.oneqjob.entity.dto.VerificationCodeDto;
import com.backend.oneqjob.global.config.RedisHash;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceImplTest {

    @Mock
    private RedisSessionRepository sessionRepository;

    @InjectMocks
    private OtpServiceImpl otpService;

    @Test
    void createSession_Success() {
        SmsRequestDto requestDto = new SmsRequestDto();
        requestDto.setPhoneNumber("+82100000000");
        String sessionId = otpService.createSession(requestDto);
        RedisHash mockSession = new RedisHash(sessionId, "+82100000000", "123456", true, 180L);

        lenient().when(sessionRepository.save(any(RedisHash.class))).thenReturn(mockSession);


        assertNotNull(sessionId);
    }

    @Test
    void beforeRequest()  {
        String sessionId = "12345";
        RedisHash mockSession = new RedisHash(sessionId, "+82100000000", "123456", true, 180L);

        lenient().when(sessionRepository.findById(eq(sessionId))).thenReturn(Optional.of(mockSession));


        SingleMessageSendingRequest mockRequest = mock(SingleMessageSendingRequest.class);
        assertNotNull(mockRequest);

    }

    @Test
    void getData() {
        String sessionId = "12345";
        RedisHash mockSession = new RedisHash(sessionId, "+82100000000", "123456", true, 180L);

        lenient().when(sessionRepository.findById(eq(sessionId))).thenReturn(Optional.of(mockSession));

        Map<String, Object> expectedResult = new HashMap<>();

        expectedResult.put("phone", "+82100000000");
        expectedResult.put("code", "123456");
        expectedResult.put("유효시간", "3분");
        expectedResult.put("id", "12345");


        Map<String, Object> actualResult = otpService.getData(sessionId);

        assertNotNull(actualResult);


        Map<String, Object> expectedResultLowercase = new HashMap<>();
        expectedResult.forEach((key, value) -> expectedResultLowercase.put(key.toLowerCase(), value));
        Map<String, Object> actualResultLowercase = new HashMap<>();
        actualResult.forEach((key, value) -> actualResultLowercase.put(key.toLowerCase(), value));

        assertEquals(expectedResultLowercase, actualResultLowercase);
    }

    @Test
    void verifyOtp() {
        OtpServiceImpl otpService = new OtpServiceImpl(sessionRepository);

        when(sessionRepository.findById(anyString())).thenReturn(Optional.of(new RedisHash("sessionId", "phoneNumber", "code", false, 180L)));

        VerificationCodeDto codeDto = new VerificationCodeDto();
        codeDto.setSessionId("sessionId");
        codeDto.setPhoneNumber("phoneNumber");
        codeDto.setCode("code");

        boolean result = otpService.verifyOtp(codeDto);

        assertTrue(result);

    }
}
