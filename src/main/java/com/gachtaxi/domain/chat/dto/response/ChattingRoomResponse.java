package com.gachtaxi.domain.chat.dto.response;

import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.chat.entity.enums.Status;

public record ChattingRoomResponse(
        Long roomId,
        Status status
) {
    public static ChattingRoomResponse from(ChattingRoom chattingRoom) {
        return new ChattingRoomResponse(chattingRoom.getId(), chattingRoom.getStatus());
    }
}
