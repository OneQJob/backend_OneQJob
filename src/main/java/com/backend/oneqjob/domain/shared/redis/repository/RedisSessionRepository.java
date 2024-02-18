package com.backend.oneqjob.domain.shared.redis.repository;
import com.backend.oneqjob.global.config.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface RedisSessionRepository extends CrudRepository<RedisHash, String> {

}
