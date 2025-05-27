package com.singingcontest.email.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * EmailRequestDto
 * - 이메일 인증 관련 API 요청 데이터를 담는 DTO
 * - 인증코드 발송 및 검증 모두 이 클래스를 통해 요청받음
 *
 * 인증 요청: email만 사용
 * 인증 검증: email + code 모두 사용
 */
@Getter
@Setter
@NoArgsConstructor
public class EmailRequestDto {
    //사용자 이메일 주소
    private String email;

    //사용자 입력 인증코드(검증 시 사용)
    private String code;
}