package com.backend.oneqjob.domain.user.service.impl;

import com.backend.oneqjob.domain.shared.redis.repository.RedisSessionRepository;
import com.backend.oneqjob.entity.dto.VerificationCodeDto;
import com.backend.oneqjob.global.config.RedisHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.domain.user.exception.ErrorCode;
import com.backend.oneqjob.domain.user.service.OtpService;
import com.backend.oneqjob.entity.dto.SmsRequestDto;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;

@Service
@Transactional
public class OtpServiceImpl implements OtpService {


    private static final Logger LOGGER = LoggerFactory.getLogger(OtpServiceImpl.class);
    @Value("${coolsms.api.sender}")
    private String sender;


    private final RedisSessionRepository sessionRepository;

    public OtpServiceImpl(RedisSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * 전화번호, 인증번호 등을 저장할 세션생성
     *
     * @param requestDto 전화번호
     * @return session (전화번호, 인증번호 )를 담아서 보냄
     * @throws CustomException 세션을 만들다가 생긴 오류
     */

    @Override
    public String createSession(SmsRequestDto requestDto) throws CustomException {
        try {
            RedisHash session = new RedisHash();
            session.setId(String.valueOf(randomUUID()));
            session.setPhoneNumber(requestDto.getPhoneNumber());
            session.setCode(makeRandomCode());
            sessionRepository.save(session);

            return session.getId();
        } catch (Exception e) {
            LOGGER.error("Error creating session", e);
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
     *
     * @param sessionId
     * @return 발신자, 수신자, 텍스트 가 담긴 객체
     * @throws CustomException request 객체 생성시 문제가 발생하면 에러 발생
     */
    @Override
    public SingleMessageSendingRequest beforeRequest(String sessionId) throws CustomException {
        RedisHash session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_ERROR));

        String phoneNumber = session.getPhoneNumber();
        try {
            if (phoneNumber == null) {
                throw new CustomException(ErrorCode.SESSION_ERROR);
            }
            Message msg = new Message();
            msg.setFrom(sender);
            msg.setTo(phoneNumber);
            String code = session.getCode();
            msg.setText("Your verification code is: " + "[" + code + "]" + ". It is valid for " + 3 + " seconds.");
            SingleMessageSendingRequest request = new SingleMessageSendingRequest(msg);
            return request;
        } catch (Exception e) {
            LOGGER.error("Failed to send SMS to {}: {}", phoneNumber, e.getMessage());
            throw new CustomException(ErrorCode.SMS_SENDING_ERROR);
        }
    }

    /**
     * 1.유저에게 인증코드를 성공적으로 보낸 후에 , 프론트에 data 에 담을 내용 response 객체 생성
     * 2.세션에 유저의 인증 유효시간인, 세션을 얼마동안 유지 할지 지정
     *
     * @param sessionId
     * @return data : 핸드폰 번호 , 코드 , 유효시간(세션유지시간)
     * @throws CustomException 세션에 유저의 인증 유효시간과 , 유지 시간 설정에 실패하면 에러 발생
     */
    @Override
    public Map<String, Object> getData(String sessionId) throws CustomException {
        Map<String, Object> data = new HashMap<>();
        RedisHash session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_ERROR));

        try {
            data.put("Id", session.getId());
            data.put("phone", session.getPhoneNumber());
            data.put("code", session.getCode());
            data.put("유효시간", "3분");
            session.setExpiration(180L);
            session.setValidStatus(false);
            sessionRepository.save(session);
            LOGGER.info("세션 정보 조회: ID={}, 전화번호={}, 코드={}, 유효시간={}초, 인증여부={}",
                    session.getId(), session.getPhoneNumber(), session.getCode(), session.getExpiration(), session.isValidStatus());

        } catch (IllegalStateException e) {
            throw new CustomException(ErrorCode.SESSION_ERROR);
        }
        return data;
    }

    /**
     * 유저의 전화번호 검증
     * @param codeDto 유효한 인증번호 ,핸드폰번호 , 세션Id
     * @return
     * @throws CustomException 인증유효시간 안에 검증하지 않으면 에러, 전화 번호와 코드가 맞지 않으면 에러
     */
        @Override
        public boolean verifyOtp (VerificationCodeDto codeDto){
            RedisHash session = sessionRepository.findById(codeDto.getSessionId()).orElse(null);
            if (session == null) {
                LOGGER.warn("Session not found. Throwing TIMEOUT exception.");
                throw new CustomException(ErrorCode.TIMEOUT);
            }

            String phone = codeDto.getPhoneNumber();
            String code = codeDto.getCode();

            String sessionPhoneNumber = session.getPhoneNumber();
            String sessionCode = session.getCode();

            LOGGER.info("Attempting to verify OTP. Session ID: {}", codeDto.getSessionId());
            LOGGER.info("Session Phone Number: {}, Provided Phone Number: {}", sessionPhoneNumber, phone);
            LOGGER.info("Session Code: {}, Provided Code: {}", sessionCode, code);

            if (sessionPhoneNumber == null || sessionCode == null) {
                LOGGER.warn("Session attributes are null. Throwing TIMEOUT exception.");
                throw new CustomException(ErrorCode.TIMEOUT);
            }

            if (!phone.equals(sessionPhoneNumber) || !code.equals(sessionCode)) {
                LOGGER.warn("Provided phone number or code does not match session. Throwing INVALID_CODE exception.");
                throw new CustomException(ErrorCode.INVALID_CODE);
            }

            session.setValidStatus(true);
            session.setExpiration(600L);
            sessionRepository.save(session);
            LOGGER.info("세션 정보 조회: ID={}, 전화번호={}, 코드={}, 유효시간={}초, 인증여부={}",
                    session.getId(), session.getPhoneNumber(), session.getCode(), session.getExpiration(), session.isValidStatus());
            LOGGER.info("OTP verification successful. Session validStatus set to true.");
            return true;
        }

    }