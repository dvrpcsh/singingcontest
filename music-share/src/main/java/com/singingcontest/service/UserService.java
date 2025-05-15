package com.singingcontest.service;

import com.singingcontest.domain.User;
import com.singingcontest.dto.LoginRequest;
import com.singingcontest.dto.LoginResponse;
import com.singingcontest.dto.UserUpdateRequest;
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
    private final RedisService redisService; //Redis 연동 추가

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
     * - AccessToken + RefreshToken 생성
     * - RefreshToken을 Redis에 저장
     * @param reqeuest 이메일+비밀번호
     * @return 로그인 결과 응답 DTO(이메일, 닉네임, accesstoken, refreshtoken포함)
     */
    public LoginResponse login(LoginRequest request) {

        //1.사용자 존재 여부 확인
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //2.비밀번호 일치 여부 확인
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //3.토큰 생성
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        //4.Redis에 RefreshToken 저장(7일 유효)
        redisService.saveRefreshToken(user.getEmail(), refreshToken, 60 * 24 * 7);//분 단위

        //5.응답 DTO 생성
        return new LoginResponse(user.getEmail(), user.getNickname(), accessToken, refreshToken);

    }

    /**
     * 사용자 정보 수정
     *
     * @param email 로그인 한 사용자 이메일(JWT에서 추출)
     * @param request 닉네임 및 비밀번호 수정 정보
     * @param 수정된 사용자 객체
     */
    @Transactional
    public User updateUser(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //닉네임이 전달되었을 경우만 수정
        if(request.getNickname() != null && !request.getNickname().isBlank()) {
            user.setNickname(request.getNickname());
        }

        //비밀번호가 전달되었을 경우만 수정
        if(request.getPassword() != null && !request.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
        }

        return userRepository.save(user); //저장 후 객체 반환
    }

    /**
     * 회원탈퇴
     * @param eamil 로그인 한 사용자 이메일(JWT에서 추출)
     */
    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);
    }
}