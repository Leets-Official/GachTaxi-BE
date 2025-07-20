package com.gachtaxi.domain.friend.repository;

import com.gachtaxi.domain.friend.entity.Friends;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friends, Long> {

    @Query("SELECT f FROM Friends f " +
            "JOIN FETCH f.sender s " +
            "JOIN FETCH f.receiver r " +
            "WHERE (s.id = :memberId OR r.id = :memberId) " +
            "AND f.status = 'ACCEPTED'")
    Slice<Friends> findFriendsListByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT f FROM Friends f WHERE" +
            "(f.sender.id = :member1Id AND f.receiver.id = :member2Id) OR" +
            "(f.sender.id = :member2Id AND f.receiver.id = :member1Id)")
    Optional<Friends> findFriendShip(Long member1Id, Long member2Id);

    Optional<Friends> findBySenderIdAndReceiverId(Long senderId, Long receiverId);


    /*
    * 친구 목록에서 내 친구 검색
    * 1. Friends 테이블에서 Sender, Receiver Join (누가 Sender, Receiver인 줄 모르니...)
    * 2. 내가 sender면 Receiver의 nickname 검색
    * 3. 내가 Receiver면 Sender의 nickname 검색
    * 4. NickName 오름차순 정렬
    * */
    @Query("""
    SELECT DISTINCT f
    FROM Friends f
    LEFT JOIN FETCH f.sender s
    LEFT JOIN FETCH f.receiver r
    WHERE f.status = 'ACCEPTED'
      AND (
           (s.id = :memberId AND LOWER(r.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')))
        OR (r.id = :memberId AND LOWER(s.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')))
      )
    ORDER BY
      CASE
        WHEN s.id = :memberId THEN LOWER(r.nickname)
        ELSE LOWER(s.nickname)
      END ASC
    """)
    Slice<Friends> findMyFriendsByNickname(
            @Param("memberId") Long memberId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
