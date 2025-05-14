package com.singingcontest.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 요청이 들어올 떄마다 실행되는 필터 로직
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 요청이 들어올 때 마다 실행되는 필터 로직
     */
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Authorization 헤더에서 토큰 추출
        String bearerToken = request.getHeader("Authorization");

        //토큰이 "Bearer"로 시작하는지 체크
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {

            String token = bearerToken.substring(7); //"Bearer " 이후 문자 추출

            //토큰 유효성 검사
            if(jwtTokenProvider.validateToken(token)) {
                //토큰에서 이메일 추출
                String email = jwtTokenProvider.getEmailFromToken(token);

                //SecurityContext에 인증 정보 등록
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,null,null);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        //다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}