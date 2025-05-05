package com.gachtaxi.domain.chat.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatMemberInfoResponse(
        Long roomId,
        Long totalParticipantCount,
        ChatMemberResponse roomMaster,
        List<ChatMemberResponse> participants
) {
    public static ChatMemberInfoResponse of(Long roomId, Long totalParticipantCount, ChatMemberResponse roomMaster, List<ChatMemberResponse> participants) {
        return ChatMemberInfoResponse.builder()
                .roomId(roomId)
                .totalParticipantCount(totalParticipantCount)
                .roomMaster(roomMaster)
                .participants(participants)
                .build();
    }
}
