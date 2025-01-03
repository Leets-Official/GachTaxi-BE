package com.gachtaxi.domain.chat.repository;

import com.gachtaxi.domain.chat.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

}
