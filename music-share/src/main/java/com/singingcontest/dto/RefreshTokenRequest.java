package com.singingcontest.dto;

import lombok.Getter;

/**
 * RefreshToken 재발급 요청 DTO
 * -클라이언트에서 전달받은 RefreshToken을 담는다.
 */
@Getter
public class RefreshTokenRequest {
    private String refreshToken;
}