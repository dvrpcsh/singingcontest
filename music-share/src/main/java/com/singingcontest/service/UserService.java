package com.singingcontest.service;

import com.singingcontest.domain.User;
import com.singingcontest.dto.LoginRequest;
import com.singingcontest.dto.LoginResponse;
import com.singingcontest.jwt.JwtTokenProvider;
import com.singingcontest.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 고나련 비즈니스 로직을 담당하는 서비스 클래스
 * 회원가입, 중복확인, 로그인 기능을 포함
 */
@Service
@RequiredArgsConstructor //생성자 주입
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 회원가입 처리
     * @param user 사용자 정보
     * @return 저장된 사용자 엔티티
     */
    @Transactional
    public User registerUser(User user) {
        //비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        //권한이 누락됐을 경우 기본 권한 부여
        if(user.getRole() == null) {
            user.setRole("USER");
        }

        //저장
        return userRepository.save(user);
    }

    /**
     * 이메일 중복 확인
     * @param email 확인할 이메일
     * @return true면 중복
     */
    public boolean isEmailDuplicated(String email) {

        return userRepository.existsByEmail(email);
    }

    /**
     * 닉네임 중복 확인
     * @param nickname 확일한 닉네임
     * @return true면 중복
     */
    public boolean isNicknameDuplicated(String nickname) {

        return userRepository.existsByNickname(nickname);
    }

    /**
     * 이메일로 사용자 조회
     * @param email 검색할 이메일
     * @return 존재하면 User객체, 없으면 null
     */
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * 로그인 처리
     * - 입력된 이메일로 사용자 검색
     * - 비밀번호 일치 여부 확인
     * - 토큰 생성 후 응답 객체(loginResponse)로 반환
     * @param request LoginRequest(email + password)
     * @return 로그인 결과 응답 DTO(이메일, 닉네임, JWT토큰 포함)
     */
    public LoginResponse login(LoginRequest request) {

        //사용자 존재 여부 확인
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //비밀번호 일치 여부 확인
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //이메일을 기반으로 JWT토큰 생성
        String token = jwtTokenProvider.generateToken(user.getEmail());

        //응답 DTO 구성 후 반환
        return new LoginResponse(user.getEmail(), user.getNickname(), token);
    }
}