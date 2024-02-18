package com.backend.oneqjob.domain.email.exception.exhandler;

import com.backend.oneqjob.domain.email.controller.EmailController;
import com.backend.oneqjob.global.api.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * EmailController 클래스에서 예외가 발생하면 모두 이 EmailExceptionHandler 클래스에서 처리
 */
@Slf4j
@RestControllerAdvice(assignableTypes = {EmailController.class})
public class EmailExceptionHandler {

    /**
     * EmailController 클래스에서 MissingServletRequestParameterException 및 그 하위 예외가 발생했을 경우 해당 메서드에서 처리
     * @param e 발생한 에외 (MissingServletRequestParameterException) : 필수적인 쿼리 파라미터 미입력 시 발생
     * @return 예외 응답 결과
     */
    @ExceptionHandler
    public ResponseEntity<ResponseDto> requestParamExHandle(MissingServletRequestParameterException e) {
        log.error("[EmailExceptionHandler > requestParamExHandle] ex", e);

        ResponseDto<Object> responseDto = new ResponseDto<>(false, "Mandatory query parameters are not existed", null);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * EmailController 클래스에서 RuntimeException 및 그 하위 예외가 발생했을 경우 해당 메서드에서 처리
     * @param e 발생한 에외 (SessionException, CodeNotMatchException)
     * @return 예외 응답 결과
     */
    @ExceptionHandler
    public ResponseEntity<ResponseDto> sessionExHandle(RuntimeException e) {
        log.error("[EmailExceptionHandler > sessionExHandle] ex", e);

        ResponseDto<Object> responseDto = new ResponseDto<>(false, e.getMessage(), null);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * EmailController 클래스에서 MethodArgumentNotValidException 및 그 하위 예외가 발생했을 경우 해당 메서드에서 처리
     * @param e 발생한 에외 (MethodArgumentNotValidException) : 건네받은 Json 데이터에 필수값이 없을 경우 발생
     * @return 예외 응답 결과
     */
    @ExceptionHandler
    public ResponseEntity<ResponseDto> ArgumentNotValidExHandle(MethodArgumentNotValidException e) {
        log.error("[EmailExceptionHandler > ArgumentNotValidExHandle] ex", e);

        ResponseDto<Object> responseDto = new ResponseDto<>(false, "Mandatory values of HTTP body are not existed", null);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
