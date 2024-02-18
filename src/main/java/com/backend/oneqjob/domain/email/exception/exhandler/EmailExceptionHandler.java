package com.backend.oneqjob.domain.email.exception.exhandler;

import com.backend.oneqjob.domain.email.controller.EmailController;
import com.backend.oneqjob.global.api.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * EmailController 클래스에서 예외가 발생하면 모두 이 EmailExceptionHandler 클래스에서 처리
 */
@Slf4j
@RestControllerAdvice(assignableTypes = {EmailController.class})
public class EmailExceptionHandler {

    /**
     * EmailController 클래스에서 RuntimeException 및 그 하위 예외가 발생했을 경우 해당 메서드에서 처리
     * @param e 발생한 에외 (SessionException, CodeNotMatchException)
     * @return 예외 응답 결과
     */
    @ExceptionHandler
    public ResponseEntity<ResponseDto> sessionExHandle(RuntimeException e) {
        log.error("[EmailExceptionHandler] ex", e);

        ResponseDto<Object> responseDto = new ResponseDto<>(false, e.getMessage(), null);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
