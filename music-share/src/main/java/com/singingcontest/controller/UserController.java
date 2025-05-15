package com.singingcontest.controller;

import com.singingcontest.domain.User;
import com.singingcontest.dto.LoginRequest;
import com.singingcontest.dto.LoginResponse;
import com.singingcontest.dto.RefreshTokenRequest;
import com.singingcontest.jwt.JwtTokenProvider;
import com.singingcontest.jwt.TokenBlacklistService;
import com.singingcontest.service.RedisService;
import com.singingcontest.service.UserService;
import com.singingcontest.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 사용자 회원가입 및 유효성 검사 API
 */
@RestController
@RequestMapping("/api/Users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RedisService redisService;

    //블랙리스트 방식을 통한 로그아웃을 위한 의존성 주입
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * 회원가입 API
     * @param user 요청 바디에 담긴 회원 정보
     * @return 등록된 사용자 정보 또는 상태 코드
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if(userService.isEmailDuplicated(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 이메일입니다.");
        }

        if(userService.isEmailDuplicated(user.getNickname())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 닉네임입니다.");
        }

        User savedUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /**
     * 이메일 중복 확인 API
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean duplicated = userService.isEmailDuplicated(email);
        return ResponseEntity.ok(duplicated);
    }

    /**
     * 닉네임 중복 확인 API
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean duplicated = userService.isNicknameDuplicated(nickname);
        return ResponseEntity.ok(duplicated);
    }

    /**
     * 로그인 API (Access + Refresh Token 발급 포함)
     * -이메일과 비밀번호를 확인하고 JWT토큰발급
     * @param request 이메일+비밀번호
     * @return 로그인 성공 시 사용자 정보 + JWT토큰 반환 + RefreshToken은 Redis에 저장(만료시간 7일)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        //로그인 처리(UserService에서 이메일/비밀번호 검증)
        LoginResponse user = userService.login(request);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        //토큰 생성
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        //RefreshToken을 Redis에 저장(7일 = 60분 * 24시간 * 7일)
        redisService.saveRefreshToken(user.getEmail(), refreshToken, 60*24*7);

        //토큰 응답 생성
        Map<String, String> tokenResponse = new HashMap<>();

        tokenResponse.put("accessToken", accessToken);
        tokenResponse.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * 현재 로그인 된 사용자 정보 조회 API
     * 요청에 포함된 JWT 토큰으로부터 사용자 정보를 식별
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo(Authentication authentication) {
        //JWT필터에서 SecurityContext에 저장된 인증 정보에서 사용자 이메일 꺼냄
        String email = (String)authentication.getPrincipal();

        //이메일로 DB에서 사용자 조회
        User user = userService.getUserByEmail(email);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        //응답: 이메일, 닉네임, 권한 등 필요한 정보만 전달
        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "role", user.getRole()
        ));

    }

    /**
     * 로그아웃 API
     * JWT는 서버에서 세션을 유지하지 않기 때문에
     * 프런트엔드(Localstorage나 쿠키)에 저장된 토큰을 삭제하므로써 로그아웃 됨
     * 서버에서는 별도 처리 없이 단순 응답만 반환
     * 추후 보안이 필요할 경우 Redis 등의 블랙리스트 저장 방식 도입 가능
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        //프런트엔드가 저장하고 있는 JWT토큰을 삭제하도록 안내
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    /**
     * 개인적 궁금함에 블랙리스트 로그아웃 구현
     * 개요: JWT를 Redis에 저장해서 토큰을 서버에서 무효화하는 방식
     * [1]: 사용자가 로그아웃을 요청함
     * [2]: 서버가 해당 JWT토큰을 Redis에 저장(만료시간까지)
     * [3]: 이후 요청이 오면 토큰이 블랙리스트에 있는지 확인하고 차단
     */
    @PostMapping("/logoutThroughBlackList")
    public ResponseEntity<?> logoutThroughBlackList(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            long expiration = jwtTokenProvider.getExpiration(token);
            tokenBlacklistService.blacklistToken(token, expiration);
        }

        return ResponseEntity.ok("로그아웃 완료 (토큰 무료화됨)");
    }

    /**
     * AccessToken 재발급 API
     * -클라이언트가 만료된 AccessToken 대신 RefreshToken을 보내면 서버가 새 AccessToken을 발급해주는 API
     *
     * @param request RefreshToken을 담은 요청 바디
     * @return 새로운 AccessToken 반환
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(@RequestBody RefreshTokenRequest request) {

        //1.클라이언트가 보낸 RefreshToken 추출
        String refreshToken = request.getRefreshToken();

        //2.RefreshToken 유효성 검사(형식 + 만료 확인)
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("유효하지 않은 리프레시 토큰입니다.");
        }

        //3.RefreshToken에서 이메일(사용자 식별자) 추출
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);

        //4.Redis에 저장된 RefreshToken을 가져옴(로그아웃 여부 또는 탈취 검증)
        String savedToken = redisService.getRefreshToken(email);

        //5.저장된 토큰이 없거나 일치하지 않으면 유효하지않은 요청 처리
        if(savedToken == null || !savedToken.equals(refreshToken)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("이미 만료되었거나 위조된 토큰입니다.");
        }

        //6. 새로운 AccessToken생성
        String newAccessToken = jwtTokenProvider.createToken(email);

        //7.응답: 새로 발급된 AccessToken만 반환
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    /**
     * 사용자 정보 수정 API
     *
     * 로그인 한 사용자의 이메일을 기준으로 닉네임/비밀번호 수정
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest request, Authentication authentication) {

        //SecurityContext에서 사용자 이메일 추출
        String email = String.valueOf(authentication.getPrincipal());

        //수정 로직 호출
        User updatedUser = userService.updateUser(email, request);

        return ResponseEntity.ok().body(Map.of(
                "message", "회원정보가 수정되었습니다.",
                "nickname", updatedUser.getNickname()
        ));
    }

    /**
     * 회원탈퇴
     * JWT기반 인증된 사용자만 자신의 계정을 삭제 가능
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        String email = String.valueOf(authentication.getPrincipal());

        userService.deleteUser(email);

        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }
}