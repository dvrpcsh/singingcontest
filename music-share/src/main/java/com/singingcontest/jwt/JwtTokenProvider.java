package com.singingcontest.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 유틸리티 클래스
 */
@Component
public class JwtTokenProvider {

    //예시 비밀키(환경변수로 분리하는 것이 좋음)
    private String secretKey = "my-sercet-key-sanghyeop-key";

    //JWT 서명을 위한 키 객체
    private Key key;

    //토큰 유효 시간(1시간)
    private final long tokenValidityInMilliseconds = 1000 * 60 * 60;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * JWT토큰생성
     */
    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;
        } catch(JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 토큰에서 이메일 추출
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    /**
     * 주어진 JWT 토큰에서 만료시간 추출
     * @param token JWT문자열
     * @param 남은 만료 시간(밀리초단위)
     */
    public long getExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey) //비밀키로 파싱
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration(); //exp클레임

        return expiration.getTime() - System.currentTimeMillis(); //현재 시간 기준으로 남은 시간(ms)
    }
}