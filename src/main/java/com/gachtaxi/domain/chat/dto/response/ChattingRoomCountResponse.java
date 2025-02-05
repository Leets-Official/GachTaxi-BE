package com.gachtaxi.domain.chat.dto.response;

public record ChattingRoomCountResponse(
        Long roomId,
        Long totalParticipantCount
) {
    public static ChattingRoomCountResponse of(Long roomId, Long totalParticipantCount) {
        return new ChattingRoomCountResponse(roomId, totalParticipantCount);
    }
}
