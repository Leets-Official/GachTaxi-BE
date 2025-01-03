package com.gachtaxi.domain.chat.repository;

import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingParticipantRepository extends JpaRepository<ChattingParticipant, Long> {
}
