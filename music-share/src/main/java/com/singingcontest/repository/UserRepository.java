package com.singingcontest.repository;

import com.singingcontest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * User 엔티티를 위한 JPA Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    //이메일로 사용자 찾기
    Optional<User> findByEmail(String email);

    //닉네임 중복 확인용 메서드
    boolean existsByNickname(String nickname);

    //이메일 중복 확인용 메서드
    boolean existsByEmail(String email);

}