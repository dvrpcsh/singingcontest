package com.singingcontest.email.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * EmailUtil
 * -인증코드 생성 및 이메일 전송을 담당하는 유틸리티 클래스
 * -이메일 인증 기능에서 공통적으로 사용되는 로직 모음
 */
@Component
@RequiredArgsConstructor
public class EmailUtil {
    //Spring Boot의 JavaMailSender 주입(의존성 필요)
    private final JavaMailSender mailSender;
    private static final int CODE_LENGTH = 6;

    /**
     * 인증코드 생성 메서드
     * - 6자리 숫자형 랜덤 문자열을 반환
     *
     * @return 인증코드 문자열
     */
    public String createRandomCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
        /**
         * 이메일 전송 메서드
         * -JavaMailSender를 사용하여 간단한 텍스트 이메일을 전송
         *
         * @param to 받는사람 이메일
         * @param subject 이메일 제목
         * @param text 내용
         */
        public void sendEmail(String to, String subject, String text) {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);           //수신자
            message.setSubject(subject); //제목
            message.setText(text);       //본문 내용

            mailSender.send(message);    //메일 전송
        }
}