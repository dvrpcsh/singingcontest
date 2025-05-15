package com.singingcontest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 성공 시 클라이언트에 전달할 응답 DTO
 * - 사용자 식별 정보와 함께 AccessToken, RefreshToken 반환
 */
@Getter
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}