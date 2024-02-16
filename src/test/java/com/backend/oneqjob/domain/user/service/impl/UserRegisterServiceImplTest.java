package com.backend.oneqjob.domain.user.service.impl;
import com.backend.oneqjob.domain.user.exception.CustomException;
import com.backend.oneqjob.domain.user.exception.ErrorCode;
import com.backend.oneqjob.domain.user.repository.LocationRepository;
import com.backend.oneqjob.domain.user.repository.UserRepository;
import com.backend.oneqjob.entity.LocationEntity;
import com.backend.oneqjob.entity.UserEntity;
import com.backend.oneqjob.entity.dto.SignUpRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserRegisterServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private LocationRepository locationRepo;

    @InjectMocks
    private UserRegisterServiceImpl service;

    private SignUpRequestDto requestDto;

    @BeforeEach
    void setUp() {
        // given: 초기 설정, 테스트에 필요한 객체 및 상태를 준비
        requestDto = new SignUpRequestDto("Test Name", "1234567890", "testFile", "testUrl", "1234-12-12", "testUser1@", "Test@1234",
                new SignUpRequestDto.AddressDto("판교로555", "경기도", "분당구", "백현동", "가자", "테스"));
    }

    @Test
    void signUpRequired_UserIdDuplicate_ThrowsException() {
        // given: 테스트 조건 설정
        String duplicateUserId = "testUser1@";
        requestDto.setUserId(duplicateUserId);

        // when: 테스트 실행 조건
        when(userRepo.findByUserId(duplicateUserId)).thenReturn(Optional.of(new UserEntity()));

        // then: 기대 결과 검증
        assertThrows(CustomException.class, () -> service.signUpRequired(requestDto), "Expected signUpRequired to throw CustomException, but it didn't.");
    }

    @Test
    void signUpRequired_IdInvalid() {
        // given: 유효하지 않은 userId 설정
        requestDto.setUserId("invalidID!");

        // when + then: 실행 및 결과 검증
        assertThatThrownBy(() -> service.signUpRequired(requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.USER_ID_FORMAT_INVALID.getDescription());
    }

    @Test
    void signUpRequired_PwInvalid() {
        // given: 유효하지 않은 userPw 설정
        requestDto.setUserPw("short");

        // when + then: 실행 및 결과 검증
        assertThatThrownBy(() -> service.signUpRequired(requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.PASSWORD_FORMAT_INVALID.getDescription());
    }

    @Test
    void signUpRequired_Success() {
        // given: 성공적인 회원가입을 위한 조건 설정
        when(userRepo.findByUserId(requestDto.getUserId())).thenReturn(Optional.empty());

        when(locationRepo.save(any(LocationEntity.class))).thenReturn(new LocationEntity());
        when(userRepo.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // when: 서비스 메소드 실행
        Map<String, Object> result = service.signUpRequired(requestDto);

        // then: 성공적인 실행 결과 검증
        assertThat(result).containsKeys("userName", "userId");
        assertThat(result.get("userId")).isEqualTo(requestDto.getUserId());
    }
}

