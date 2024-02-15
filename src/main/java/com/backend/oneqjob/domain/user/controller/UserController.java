package com.backend.oneqjob.domain.user.controller;

import com.backend.oneqjob.domain.user.common.Helper;
import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.domain.user.service.UserRegisterService;
import com.backend.oneqjob.entity.dto.SignUpRequestDto;
import com.backend.oneqjob.global.api.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRegisterService userRegisterService;

    /**
     * 회원가입컨트롤러
     * @param requestDto
     * @return (successCode+Message+서비스에서 반환한 값)
     * @throws 1.필수 필드를 채우지 않았을때 ,2.서비스 유효성 검증에서 에러 ,3.예외
     */
    @PostMapping("/user/signUp")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody SignUpRequestDto requestDto) {
        String missingFields = Helper.checkRequired(requestDto);
        if (!missingFields.isEmpty()) {
            ResponseDto errorResponse = new ResponseDto(false, "Missing or invalid fields: " + missingFields, null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            Map<String, Object> responseData = userRegisterService.signUpRequired(requestDto);
            ResponseDto responseDto = new ResponseDto(true, "user create successfully", responseData);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (CustomException e) {
            ResponseDto errorResponse = new ResponseDto(false, e.getErrorCode().getDescription(), null);
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(errorResponse, status);
        } catch (Exception e) {
            ResponseDto errorResponse = new ResponseDto(false, "Internal Server Error", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
