package com.gachtaxi.domain.chat.dto.request;

import com.gachtaxi.domain.chat.entity.enums.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessage(
        Long roomId,
        Long senderId,
        String senderName,
        String message,
        LocalDateTime timeStamp,
        MessageType messageType
) {
    public static ChatMessage of(ChatMessageRequest request, long roomId, long senderId, String senderName) {
        return ChatMessage.builder()
                .roomId(roomId)
                .senderId(senderId)
                .senderName(senderName)
                .message(request.message())
                .timeStamp(LocalDateTime.now())
                .messageType(MessageType.MESSAGE)
                .build();
    }

    public static ChatMessage of(long roomId, Long senderId, String senderName, String message, MessageType messageType) {
        return ChatMessage.builder()
                .roomId(roomId)
                .senderId(senderId)
                .senderName(senderName)
                .message(message)
                .timeStamp(LocalDateTime.now())
                .messageType(messageType)
                .build();
    }
}
