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

    //예시 비밀키(운영 환경에서는 환경변수나 암호화된 config에서 불러오는 것이 좋음)
    private String secretKey = "my-sercet-key-sanghyeop-key";

    //JWT 서명을 위한 키 객체
    private Key key;

    //Access Token 유효 시간: 1시간
    private final long accessTokenValidityInMilliseconds = 1000 * 60 * 60;

    //Refresh Token 유효 시간:7일
    private final long refreshTokenValidityInMilliseconds = 1000L * 60 * 60 * 24 * 7;

    /**
     * Bean 생성 후 key초기화
     * secretKey 문자열을 이용하여 HMAC-SHA 암호화 키 생성
     */
    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * AccessToken 생성
     *
     * @param email 사용자의 고유 식별자(Subject에 해당)
     * @return JWT Access Token 문자열
     */
    public String createToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * RefreshToken 생성
     *
     * @param email 사용자의 고유 식별자
     * @return JWT Refresh Token 문자열
     */
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 유효성 검증
     * - 토큰 서명, 구조, 만료일 확인
     *
     * @param token 검사할 JWT문자열
     * @return 유효하면 true, 유효하지 않으면 false 반환
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
     *
     * @param token JWT 문자열
     * @return 이메일 문자열
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