package com.backend.oneqjob.global.config;
import com.backend.oneqjob.domain.shared.redis.repository.RedisSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class RedisSessionRepositoryTest {

   @Autowired
    private RedisSessionRepository repository;

    @Test
    public void testSaveAndFindById() {
        // 세션 정보 생성
        String sessionId = "testSessionId";
        String phoneNumber = "+821012345678";
        String code = "123456";
        boolean validStatus = true;
        long expiration = 60L; // 60초

        RedisHash newSession = new RedisHash(sessionId, phoneNumber, code, validStatus, expiration);

        // 세션 정보 저장
        repository.save(newSession);

        // 저장된 세션 정보 조회
        RedisHash foundSession = repository.findById(sessionId).orElse(null);

        // 검증
        assertNotNull(foundSession);
        assertEquals(phoneNumber, foundSession.getPhoneNumber());
        assertEquals(code, foundSession.getCode());
        assertEquals(validStatus, foundSession.isValidStatus());
        assertEquals(expiration, foundSession.getExpiration());
    }
}
