package com.singingcontest.config;

import com.singingcontest.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 관련 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // JWT 인증 필터 주입
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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

    /**
     * 인증 매니저 Bean 등록
     * 로그인 시 사용자 인증을 처리하는 핵심 컴포넌트
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    /**
     * 보안 필터 체인 설정
     * JWT기반 인증 처리
     * 세션 사용 X
     * 특정 경로만 인증 없이 허용
     */
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //CSRF토큰사용X
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //JWT기반: 세션 X
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/signup",
                                "/api/users/login",
                                "/api/users/check-email",
                                "/api/users/check-nickname"
                        ).permitAll() //이 경로들은 인증 없이 접근 허용
                        .anyRequest().authenticated() //그 외 모든 요청은 인증 필수
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); //JWT 필터 등록

        return http.build();
    }
}
