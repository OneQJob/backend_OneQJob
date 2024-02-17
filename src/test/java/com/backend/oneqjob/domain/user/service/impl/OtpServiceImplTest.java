package com.backend.oneqjob.domain.user.service.impl;

import com.backend.oneqjob.domain.user.exception.CustomException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;
import com.backend.oneqjob.entity.dto.SmsRequestDto;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

class OtpServiceImplTest {

    @Mock
    private HttpSession session;

    @InjectMocks
    private OtpServiceImpl sendOtpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSession_Success() {
        // Given
        SmsRequestDto requestDto = new SmsRequestDto();
        requestDto.setPhoneNumber("01012345678");

        // When
        sendOtpService.createSession(session, requestDto);

        // Then
        verify(session).setAttribute("phoneNumber", "01012345678");
        verify(session).setAttribute(eq("code"), anyString());
        verify(session).setAttribute("validStatus", false);
    }

    @Test
    void beforeRequest_Success() throws CustomException {
        // Given
        when(session.getAttribute("phoneNumber")).thenReturn("01012345678");
        when(session.getAttribute("code")).thenReturn("123456");

        // When
        var request = sendOtpService.beforeRequest(session);

        // Then
        assertNotNull(request);
        assertEquals("01012345678", request.getMessage().getTo());
        assertTrue(request.getMessage().getText().contains("[123456]"));
    }

    @Test
    void getData_Success() {
        // Given
        when(session.getAttribute("phoneNumber")).thenReturn("01012345678");
        when(session.getAttribute("code")).thenReturn("123456");

        // When
        Map<String, Object> data = sendOtpService.getData(session);

        // Then
        assertEquals("01012345678", data.get("phone"));
        assertEquals("123456", data.get("code"));
        assertEquals(3, data.get("유효시간"));
    }
}
