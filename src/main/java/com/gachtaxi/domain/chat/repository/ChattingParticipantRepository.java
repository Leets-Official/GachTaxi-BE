package com.gachtaxi.domain.chat.repository;

import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.members.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChattingParticipantRepository extends JpaRepository<ChattingParticipant, Long> {

    Optional<ChattingParticipant> findByChattingRoomAndMembers(ChattingRoom chattingRoom, Members member);

    Optional<ChattingParticipant> findByChattingRoomIdAndMembersId(long roomId, long memberId);
}
