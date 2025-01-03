package com.gachtaxi.domain.members.repository;

import com.gachtaxi.domain.members.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByStudentNumber(String studentNumber);

    Optional<Members> findByKakaoId(Long kakaoId);
}
