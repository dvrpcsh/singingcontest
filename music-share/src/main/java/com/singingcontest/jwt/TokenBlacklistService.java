package com.singingcontest.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 *  JWT 블랙리스트 저장/조회 서비스
 *  로그아웃 한 토큰을 Redis에 저장하여 더 이상 사용하지 못하게 막음
 */
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 토큰을 블랙리스트에 추가 (만료 시간까지 보관)
     * @param token JWT 토큰
     * @param expirationMillis 만료 시간 (ms)
     */
    public void blacklistToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(token, "logout", Duration.ofMillis(expirationMillis));
    }

    /**
     * 해당 토큰이 블랙리스트에 등록되어있는지 확인
     */
    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}