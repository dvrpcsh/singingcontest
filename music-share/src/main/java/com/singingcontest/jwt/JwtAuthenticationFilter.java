package com.singingcontest.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

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

        //1.Authorization 헤더에서 토큰 추출
        String bearerToken = request.getHeader("Authorization");

        //토큰이 "Bearer"로 시작하는지 체크
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {

            String token = bearerToken.substring(7); //"Bearer " 이후 문자 추출

            try {
                //2.토큰 유효성 검사
                if(jwtTokenProvider.validateToken(token)) {
                    //3.토큰에서 이메일 추출
                    String email = jwtTokenProvider.getEmailFromToken(token);

                    //4,SecurityContext에 인증 정보 등록
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,null,null);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                logger.warn("JWT 토큰 만료: {}"+ e.getMessage());
                throw new BadCredentialsException("JWT 토큰이 만료되었습니다.");
            } catch (MalformedJwtException e) {
                logger.warn("JWT 형식 오류: {}"+ e.getMessage());
                throw new BadCredentialsException("JWT 형식이 올바르지 않습니다.");
            } catch (Exception e) {
                logger.warn("기타 JWT 처리 오류: {}"+ e.getMessage());
                throw new BadCredentialsException("JWT 처리 중 알 수 없는 오류가 발생하였습니다.");
            }
        }

        //5.다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}