package com.gachtaxi.domain.chat.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessage(
        Long roomId,
        Long senderId,
        String nickname,
        String message,
        LocalDateTime timeStamp
) {
    public static ChatMessage of(ChatMessageRequest request, long senderId, LocalDateTime timeStamp) {
        return ChatMessage.builder()
                .roomId(request.roomId())
                .senderId(senderId)
                .nickname(request.nickname())
                .message(request.message())
                .timeStamp(timeStamp)
                .build();
    }
}
