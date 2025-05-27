package com.singingcontest.email.controller;

import com.singingcontest.email.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 📬 TestMailController
 * - 메일 발송 기능이 정상 동작하는지 확인하기 위한 테스트용 API
 */
@RestController
@RequestMapping("/api/test-mail")
@RequiredArgsConstructor
public class TestMailController {

    private final EmailUtil emailUtil;

    /**
     * ✅ 테스트 메일 발송 API
     * 사용자가 입력한 이메일로 테스트 메일을 발송함
     * @param to 받는 사람 이메일 주소 (QueryParam)
     * @return 성공 메시지
     */
    @GetMapping
    public ResponseEntity<String> sendTestMail(@RequestParam String to) {
        // 메일 제목과 본문 구성
        String subject = "[🎵 SingingContest] 테스트 메일입니다!";
        String content = "이 메일은 메일 발송 기능을 테스트하기 위한 메일입니다.\n정상적으로 도착했다면 이메일 설정이 완료된 것입니다. 🎉";

        // 실제 이메일 발송
        emailUtil.sendEmail(to, subject, content);

        return ResponseEntity.ok("✅ 테스트 메일이 " + to + " 로 발송되었습니다.");
    }
}
