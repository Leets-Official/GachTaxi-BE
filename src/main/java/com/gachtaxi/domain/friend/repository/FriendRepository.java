package com.gachtaxi.domain.friend.repository;

import com.gachtaxi.domain.friend.dto.response.FriendsResponseDto;
import com.gachtaxi.domain.friend.entity.Friends;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friends, Long> {

    @Query("SELECT new com.gachtaxi.domain.friend.dto.response.FriendsResponseDto( " +
            "CASE WHEN f.sender.id = :memberId THEN f.receiver.id ELSE f.sender.id END, " +
            "CASE WHEN f.sender.id = :memberId THEN f.receiver.nickname ELSE f.sender.nickname END, " +
            "CASE WHEN f.sender.id = :memberId THEN f.receiver.profilePicture ELSE f.sender.profilePicture END, " +
            "CASE WHEN f.sender.id = :memberId THEN f.receiver.gender ELSE f.sender.gender END " +
            ") FROM Friends f " +
            "WHERE f.status = 'ACCEPTED' " +
            "AND (f.sender.id = :memberId OR f.receiver.id = :memberId)")
    List<FriendsResponseDto> findAcceptedFriendsByMemberId(@Param("memberId") Long memberId);


    @Query("SELECT f FROM Friends f WHERE" +
            "(f.sender.id = :member1Id AND f.receiver.id = :member2Id) OR" +
            "(f.sender.id = :member2Id AND f.receiver.id = :member1Id)")
    Optional<Friends> findFriendShip(Long member1Id, Long member2Id);

    Optional<Friends> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
