package com.singingcontest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 관련 설정 클래스
 * CORS 정책을 설정하여 프론트엔드(예: React)와의 통신을 허용한다.
 */
@Configuration
public class WebConfig {

    /**
     * CORS 설정을 위한 WebMvcConfigurer Bean 등록
     * - React 프론트엔드에서 백엔드로의 요청을 허용하기 위해 설정됨
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new CustomCorsConfigurer();
    }

    /**
     * CORS 매핑을 위한 WebMvcConfigurer 구현 클래스
     */
    private static class CustomCorsConfigurer implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    }
}
