package com.backend.oneqjob.domain.user.controller;
import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.entity.dto.SmsRequestDto;
import com.backend.oneqjob.domain.user.service.OtpService;
import com.backend.oneqjob.entity.dto.VerificationCodeDto;
import com.backend.oneqjob.global.api.ResponseDto;
import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class SmsController {

    private final OtpService otpService;
    private final DefaultMessageService messageService;

    public SmsController(OtpService otpService,
                         @Value("${coolsms.api.key}") String apiKey,
                         @Value("${coolsms.api.secret}") String apiSecret,
                         @Value("${coolsms.api.url}") String apiUrl) {
        this.otpService = otpService;
        this.messageService = new DefaultMessageService(apiKey, apiSecret, apiUrl);
    }

    @PostMapping("/sendOtp")
    public ResponseEntity<ResponseDto> sendOtp(@RequestBody SmsRequestDto otpRequest) {
        try {
            String s = otpService.createSession(otpRequest);
            SingleMessageSendingRequest sendingRequest = otpService.beforeRequest(s);
            messageService.sendOne(sendingRequest);
            Map<String, Object> data = otpService.getData(s);
            ResponseDto<Map<String, Object>> responseDto = new ResponseDto<>(true, "OTP code was successfully sent", data);
            return ResponseEntity.ok(responseDto);
        } catch (CustomException e) {
            ResponseDto<Void> responseDto = new ResponseDto<>(false, e.getMessage(), null);
            return ResponseEntity.badRequest().body(responseDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/checkOtp")
    public ResponseEntity<ResponseDto> checkOtp(@RequestBody VerificationCodeDto otpCodeRequest) {
        try {
            boolean isValid = otpService.verifyOtp(otpCodeRequest);
            if (isValid) {
                ResponseDto responseDto = new ResponseDto(true, "Authentication code verification succeeded.", null);
                return ResponseEntity.ok(responseDto);
            } else {
                ResponseDto responseDto = new ResponseDto(false, "Verification failed due to unexpected error.", null);
                return ResponseEntity.badRequest().body(responseDto);
            }
        } catch (CustomException e) {
            ResponseDto responseDto = new ResponseDto(false, e.getMessage(), null);
            return ResponseEntity.badRequest().body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(false, "An error occurred during verification.", null);
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
}


