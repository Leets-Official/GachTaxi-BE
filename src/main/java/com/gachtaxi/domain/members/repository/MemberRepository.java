package com.gachtaxi.domain.members.repository;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.entity.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByEmail(String email);

    Optional<Members> findByIdAndStatus(Long id, UserStatus status);

    Optional<Members> findByStudentNumber(Long studentNumber);

    Optional<Members> findByKakaoId(Long kakaoId);

    Optional<Members> findByGoogleId(String googleId);

    Optional<Members> findByNickname(String nickname);

    Optional<Members> findByNicknameAndStatus(String nickname, UserStatus status);

    Optional<Members> findByEmailAndStatus(String email, UserStatus status);

    List<Members> findByIdIn(List<Long> ids);
}
