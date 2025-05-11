package com.singingcontest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

/**
 * JWT 설정 파일 (JwtConfig)
 * - JWT (JSON Web Token) 생성 및 검증을 담당하는 클래스
 */
@Component
public class JwtConfig {

    // JWT 비밀 키 (application.properties에서 로드)
    @Value("${jwt.secret}")
    private String jwtSecret;

    // JWT 만료 시간 (밀리초, application.properties에서 로드)
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * JWT 토큰 생성 메소드
     *
     * @param email 사용자 이메일 (토큰의 subject로 설정)
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}