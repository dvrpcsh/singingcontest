package com.singingcontest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis 관련 설정 클래스
 */
@Configuration
public class RedisConfig {
    /**
     * RedisTemplate Bean 등록
     * - 문자열 기반의 Redis 명령어를 사용할 수 있또록 설정
     * ex) redisTemplate.posForValue().set("Key","value");
     */
    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {

        return new StringRedisTemplate(connectionFactory);
    }
}