package com.backend.oneqjob.global.config;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.TimeToLive;


@Getter
@Setter
@AllArgsConstructor
@org.springframework.data.redis.core.RedisHash(value = "session")
public class RedisHash {

    @Id
    private String id;
    private String phoneNumber;
    private String code;
    private boolean validStatus;
    @TimeToLive
    private Long expiration;

    public RedisHash() {

    }

}
