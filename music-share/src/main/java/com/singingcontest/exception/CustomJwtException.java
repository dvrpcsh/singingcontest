package com.singingcontest.exception;

/**
 * JWT관련 오류 발생 시 던지는 커스텀 예외처리
 */
public class CustomJwtException extends RuntimeException {
    public CustomJwtException(String message) {
        super(message);
    }
}
