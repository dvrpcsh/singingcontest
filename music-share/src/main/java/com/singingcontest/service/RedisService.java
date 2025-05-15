package com.singingcontest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis를 통해 Refresh Token을 저장/조회/삭제하는 서비스
 *
 * ex)
 * 1.저장
 * redisService.saveRefreshToken("user1@example.com", "jwt-refresh-token", 1440); //1일
 *
 * 2.조회
 * String token = redisService.getRefreshToken("user1@example.com");
 *
 * 3.삭제
 * redisService.deleteRefreshToken("user1@example.com");
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    /**
     * Refresh Token 저장
     *
     * @param key 사용자 식별자 (예: email 또는 userId)
     * @param token 저장할 Refresh Token
     * @param expirationMinutes 만료 시간(분 단위)
     */
    public void saveRefreshToken(String key, String Token, long expirationMinutes) {
        redisTemplate.opsForValue().set(key, token, expirationMinutes, TimeUnit.MINUTES);
    }

    /**
     * Refresh Token 조회
     *
     * @param key 사용자 식별자
     * @return 저장된 Refresh Token (없으면 null)
     */
    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Refresh Token 삭제
     *
     * @param key 사용자 식별자
     */
    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }

}