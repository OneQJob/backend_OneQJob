package com.backend.oneqjob.domain.user.controller;

        import com.backend.oneqjob.domain.shared.entity.dto.OtpRequest;
        import com.backend.oneqjob.domain.shared.service.PhoneValidationService;
        import com.backend.oneqjob.global.api.ResponseDto;
        import jakarta.servlet.http.HttpSession;
        import net.nurigo.sdk.message.model.Message;
        import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
        import net.nurigo.sdk.message.service.DefaultMessageService;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.time.LocalDateTime;
        import java.time.format.DateTimeFormatter;
        import java.util.HashMap;
        import java.util.Map;
@RestController
@RequestMapping("/user")
public class SmsController {

    private final PhoneValidationService phoneValidationService;
    private final DefaultMessageService defaultMessageService;

    public SmsController(PhoneValidationService phoneValidationService,
                         @Value("${coolsms.api.key}") String apiKey,
                         @Value("${coolsms.api.secret}") String apiSecret,
                         @Value("${coolsms.api.url}") String apiUrl) {
        this.phoneValidationService = phoneValidationService;
        this.defaultMessageService = new DefaultMessageService(apiKey, apiSecret, apiUrl);
    }

    @PostMapping("/sendOtp")
    public ResponseEntity<ResponseDto> sendOtp(@RequestBody OtpRequest otpRequest, HttpSession httpSession) {

        String phoneNumber = otpRequest.getPhoneNumber();
        try {

            String validationCode = phoneValidationService.getValidationCode();
            Message message = new Message();
            message.setFrom("01096080768");
            message.setTo(phoneNumber);
            message.setText("인증번호: [" + validationCode + "]");

            SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
            defaultMessageService.sendOne(request);

            // 세션에 인증 코드와 메시지 ID 저장
            httpSession.setAttribute("phoneNumber", phoneNumber);
            httpSession.setAttribute("validationCode", validationCode);
            httpSession.setMaxInactiveInterval(180);

            // 응답 데이터에 추가 정보 포함
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("phoneNumber", phoneNumber);
            responseData.put("validCode", validationCode);
            responseData.put("timeStamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

            ResponseDto responseDto = new ResponseDto(true, "OTP code was successfully sent.", responseData);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(false, "Failed to send OTP.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
