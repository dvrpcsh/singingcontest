package com.singingcontest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 관련 설정 클래스
 */
@Configuration
public class SecurityConfig {

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
     * - BCrypt 해싱 알고리즘을 사용하는 Spring Security 기본 제공 인코더
     * - 매번 다른 해시를 생성하여 보안성이 뛰어남
     * - UserService에서 비밀번호 저장 시 사용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
