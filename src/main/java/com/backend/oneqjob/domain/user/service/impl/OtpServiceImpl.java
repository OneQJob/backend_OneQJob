package com.backend.oneqjob.domain.user.service.impl;

import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.domain.user.exception.ErrorCode;
import com.backend.oneqjob.domain.user.service.OtpService;
import com.backend.oneqjob.entity.dto.SmsRequestDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    @Value("${coolsms.api.sender}")
    private String sender;

    /**
     * 전화번호, 인증번호 등을 저장할 세션생성
     * @param session
     * @param requestDto 전화번호
     * @return session (전화번호, 인증번호 )를 담아서 보냄
     * @throws CustomException 세션을 만들다가 생긴 오류
     */
    @Override
    public HttpSession createSession(HttpSession session, SmsRequestDto requestDto) throws CustomException {
        try {
            session.setAttribute("phoneNumber", requestDto.getPhoneNumber());
            session.setAttribute("code", makeRandomCode());
            return session;
        } catch (Exception e){
            throw new CustomException(ErrorCode.SESSION_ERROR);
        }
    }

    private String makeRandomCode() {
        String ranStr = "";
        for (int i = 0; i < 6; i++) {
            ranStr += (int) (Math.random() * 10);
        }
        return ranStr;
    }

    /**
     * 외부 api 인 DefaultMessageService 는 기본인자값으로 발신자, 수신자, 텍스트를 받아야함
     * 기본인자값을 request객체로 만들어서 외부api 를 호출하기 위한 과정
     * @param session
     * @return 발신자, 수신자, 텍스트 가 담긴 객체
     * @throws CustomException request 객체 생성시 문제가 발생하면 에러 발생
     */
    @Override
    public SingleMessageSendingRequest beforeRequest(HttpSession session) throws CustomException {
        try {
            String phoneNumber = (String) session.getAttribute("phoneNumber");
            if (phoneNumber == null) {
                throw new CustomException(ErrorCode.SESSION_ERROR);
            }
            Message msg = new Message();
            msg.setFrom(sender);
            msg.setTo(phoneNumber);
            String code = (String) session.getAttribute("code");
            msg.setText("Your verification code is: " +"["+ code +"]"+". It is valid for " + 3 + " seconds.");
            SingleMessageSendingRequest request = new SingleMessageSendingRequest(msg);
            return request;
        }catch (Exception e){
            throw  new CustomException(ErrorCode.SMS_SENDING_ERROR);
        }
    }

    /**
     * 1.유저에게 인증코드를 성공적으로 보낸 후에 , 프론트에 data 에 담을 내용 response 객체 생성
     * 2.세션에 유저의 인증 유효시간인, 세션을 얼마동안 유지 할지 지정
     * @param session
     * @return data : 핸드폰 번호 , 코드 , 유효시간(세션유지시간)
     * @throws CustomException 세션에 유저의 인증 유효시간과 , 유지 시간 설정에 실패하면 에러 발생
     */
    @Override
    public Map<String, Object> getData(HttpSession session) throws CustomException {
        Map<String, Object> data = new HashMap<>();
        try {
            data.put("phone", session.getAttribute("phoneNumber"));
            data.put("code", session.getAttribute("code"));
            data.put("유효시간", "3분");
            session.setMaxInactiveInterval(180);
        }catch (IllegalStateException e){
            throw new CustomException(ErrorCode.SESSION_ERROR);
        }
        return data;
    }
}

