package com.backend.oneqjob.global.config;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisSetAndGetTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testSetAndGet() {
        String key = "testKey";
        String value = "testValue";
        redisTemplate.opsForValue().set(key, value);

        String retrievedValue = redisTemplate.opsForValue().get(key);
        assertEquals(value, retrievedValue);
    }
}

