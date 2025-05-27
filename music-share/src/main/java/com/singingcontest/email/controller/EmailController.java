package com.singingcontest.email.controller;

import com.singingcontest.email.dto.EmailRequestDto;
import com.singingcontest.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * EmailController
 * - 사용자로부터 이메일 주소를 받아 인증코드를 발송하는 API를 담당
 * - 이메일 인증코드 검증 요청도 이 컨트롤러에서 처리
 */
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    //실제 이메일 발송 및 검증을 처리할 서비스 계층 주입
    private final EmailService emailService;

    /**
     * 인증코드 발송 요청 API
     * - 사용자가 이메일 주소를 보내면 해당 주소로 인증코드를 전송함
     * - 인증코드는 EmailService에서 랜덤으로 생성되어 Redis에 저장됨
     * - 이후 JavaMailSender를 통해 메일 발송됨
     *
     * @param requestDto 사용자의 이메일 정보를 담은 DTO
     * @return 200 OK
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody EmailRequestDto requestDto) {
        emailService.sendVerificationCode(requestDto.getEmail());

        return ResponseEntity.ok("인증 코드가 발송되었습니다.");
    }

    /**
     * 인증코드 검증 요청 API
     * - 사용자가 이메일과 인증코드를 입력하면 유효성 검증을 수행
     * - 이메일에 대응하는 Redis의 코드와 입력값을 비교함
     *
     * @param requestDto 이메일 + 인증코드 포함 DTO
     * @return 200 OK / 401 Unauthorized(실패)
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody EmailRequestDto requestDto) {
        boolean isVerified = emailService.verifyCode(requestDto.getEmail(), requestDto.getCode());

        if(isVerified) {
            return ResponseEntity.ok("인증이 완료되었습니다.");
        } else {
            return ResponseEntity.status(401).body("인증에 실패하였습니다.");
        }
    }

}