package com.singingcontest.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 정보 수정 요청
 */
@Getter
@Setter
public class UserUpdateRequest {
    private String nickname;
    private String password;
}