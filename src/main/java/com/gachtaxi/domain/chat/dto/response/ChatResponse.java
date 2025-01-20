package com.gachtaxi.domain.chat.dto.response;

import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ChatResponse(
        Long memberId,
        LocalDateTime disconnectedAt,
        List<ChattingMessageResponse> chattingMessage,
        ChatPageableResponse pageable
) {
    public static ChatResponse of(ChattingParticipant chattingParticipant, List<ChattingMessageResponse> chattingMessages, ChatPageableResponse chatPageableResponse) {
        return ChatResponse.builder()
                .memberId(chattingParticipant.getMembers().getId())
                .disconnectedAt(chattingParticipant.getLastReadAt())
                .chattingMessage(chattingMessages)
                .pageable(chatPageableResponse)
                .build();
    }
}
