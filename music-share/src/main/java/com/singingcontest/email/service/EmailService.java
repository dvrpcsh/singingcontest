package com.singingcontest.email.service;

import com.singingcontest.email.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit.TimeUnit;

/**
 * EmailService
 * - 인증코드 생성 및 이메일 전송
 * - Redis를 활용한 인증코드 저장/검증
 */
@Service
@RequiredArgsConstructor
public class EmailService {
    //이메일 전송 유틸리티 클래스
    private final EmailUtil emailUtil;

    //Redis 문자열 키-값 저장소 주입
    private final StringRedisTemplate redisTemplate;

    //인증코드 유효시간(단위: 초_
    private static final long CODE_TTL = 180;

    /**
     * 인증코드 생성 및 이메일 발송
     * 1.랜덤한 인증코드 생성
     * 2.Redis에 [email:code]저장 (180초 TTL)
     * 3.사용자 이메일로 코드 전송
     *
     * @param email 사용자 이메일
     */
    public void sendVerificationCode(String email) {
        //1.랜덤6자리 인증코드 생성
        String code = emailUtil.createRandomCode();

        //2.Redis에 저장(TTL: 180초 = 3분)
        redisTemplate.opsForValue().set(email, code, CODE_TTL, TimeUnit.SECONDS);

        //3.실제 이메일로 인증코드 전송
        emailUtil.sendEmail(email, "SingingContest 인증코드 입니다.","CODE: "+ code);
    }

    /**
     * 인증코드 검증
     * -Redis에 저장된 코드와 사용자가 입력한 코드가 일치하는지 비교
     *
     * @param email 사용자 이메일
     * @param inputCode 사용자가 입력한 인증코드
     * @return 인증 성공 여부
     */
    public boolean verifyCode(String email, String inputCode) {
        //Redis에서 해당 이메일의 코드 조회
        String savedCode = redisTemplate.opsForValue().get(email);

        //코드가 존재하고, 입력값과 일치하는지 확인
        return savedCode != null && savedCode.equals(inputCode);
    }

}