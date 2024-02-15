package com.backend.oneqjob.domain.user.service.impl;

import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.domain.user.exception.ErrorCode;
import com.backend.oneqjob.domain.user.repository.LocationRepository;
import com.backend.oneqjob.domain.user.repository.UserRepository;
import com.backend.oneqjob.domain.user.service.UserRegisterService;
import com.backend.oneqjob.entity.LocationEntity;
import com.backend.oneqjob.entity.UserEntity;
import com.backend.oneqjob.entity.dto.SignUpRequestDto;

import com.backend.oneqjob.global.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;


@Service
@Transactional
@RequiredArgsConstructor
public class UserRegisterServiceImpl implements UserRegisterService {

    private final UserRepository userRepo;
    private final LocationRepository locationRepo;


    /**
     * 회원가입
     *
     * @param requestDto
     * @return Map "userName":(유저이름) ,"userId":(유저아이디)
     * @throws Exception 1. userId  중복확인 ,
     *                   2. userId  정규 표현식: 8~16자리 , 영문 대소문자만포함, 특수문자 포함가능
     *                   3. userPw  비밀번호 유효성 : 8자리 ~16자리 , 영문 대문자 소문자 포함, 특수문자 포함
     */
    @Override
    public Map<String, Object> signUpRequired(SignUpRequestDto requestDto) throws Exception {
        checkDuplicateId(requestDto.getUserId());
        checkIdValidity(requestDto.getUserId());
        checkPwValidity(requestDto.getUserPw());

        LocationEntity location = convertAddressDtoToLocationEntity(requestDto.getAddress());
        LocationEntity savedLocation = locationRepo.save(location);

        UserEntity user = convertSignUpRequestDtoToUserEntity(requestDto, savedLocation);

        user.setUserPw(PasswordUtils.hashPassword(user.getUserPw()));
        userRepo.save(user);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("userName", requestDto.getUserName());
        resultData.put("userId", requestDto.getUserId());
        return resultData;
    }


    private LocationEntity convertAddressDtoToLocationEntity(SignUpRequestDto.AddressDto addressDto) {
        LocationEntity location = new LocationEntity();
        BeanUtils.copyProperties(addressDto, location);
        return location;
    }

    private UserEntity convertSignUpRequestDtoToUserEntity(SignUpRequestDto signUpRequestDto, LocationEntity savedLocation) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(signUpRequestDto, user);
        user.setAddress(savedLocation);
        return user;
    }

    private void checkDuplicateId(String userId) throws CustomException {
        Optional<UserEntity> existingUser = userRepo.findByUserId(userId);
        if (existingUser.isPresent()) {
            throw new CustomException(ErrorCode.USER_ID_DUPLICATE);
        }
    }

    private void checkIdValidity(String userId) throws CustomException {
        String idPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$]{8,16}$";
        if (!Pattern.matches(idPattern, userId)) {
            throw new CustomException(ErrorCode.USER_ID_FORMAT_INVALID);
        }
    }

    private void checkPwValidity(String userPw) throws CustomException {
        String pwPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$";
        if (!Pattern.matches(pwPattern, userPw)) {
            throw new CustomException(ErrorCode.PASSWORD_FORMAT_INVALID);
        }
    }
}

