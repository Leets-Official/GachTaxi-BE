package com.gachtaxi.domain.friend.repository;

import com.gachtaxi.domain.friend.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friends, Long> {

    @Query("SELECT f FROM Friends f WHERE" +
            "(f.sender.id = :member1Id AND f.receiver.id = :member2Id) OR" +
            "(f.sender.id = :member2Id AND f.receiver.id = :member1Id)")
    Optional<Friends> findFriendShip(Long member1Id, Long member2Id);

}
