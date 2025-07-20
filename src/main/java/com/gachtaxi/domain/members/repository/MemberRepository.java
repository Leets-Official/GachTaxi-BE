package com.gachtaxi.domain.members.repository;

import com.gachtaxi.domain.friend.entity.enums.FriendStatus;
import com.gachtaxi.domain.members.dto.response.MemberWithFriendRequestProjection;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.entity.enums.UserStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Slice<Members> findByNicknameContaining(String nickname, Pageable pageable);

    @Query("""
    SELECT 
        NEW com.gachtaxi.domain.members.dto.response.MemberWithFriendRequestProjection(m, 
            CASE WHEN f.sender.id = :currentUserId THEN true ELSE false 
            END
        )
    FROM 
        Members m
        LEFT JOIN Friends f ON (
            f.receiver = m 
            AND f.sender.id = :currentUserId
        )
    WHERE m.nickname LIKE %:nickname%
    ORDER BY m.nickname
    """)
    Slice<MemberWithFriendRequestProjection> findMembersWithFriendRequestStatus(
            @Param("nickname") String nickname,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );
}
