package com.singingcontest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User Entity class
 * 회원정보를 관리
 */
@Entity
@Table(name= "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id //기본키설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 증가(auto_increment) 설정
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String mail; //사용자 이메일(로그인id)

    @Column(nullable = false, length = 100)
    private String password; //비밀번호

    @Column(nullable = false, length = 30)
    private String nickname; //사용자 닉네임

    @Column(nullable = false)
    private String role = "USER"; //기본권한


}