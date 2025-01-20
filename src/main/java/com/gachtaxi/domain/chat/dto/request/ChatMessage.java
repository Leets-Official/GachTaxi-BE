package com.gachtaxi.domain.chat.dto.request;

import com.gachtaxi.domain.chat.dto.response.ReadMessageRange;
import com.gachtaxi.domain.chat.entity.ChattingMessage;
import com.gachtaxi.domain.chat.entity.enums.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessage(
        String messageId,
        Long roomId,
        Long senderId,
        String senderName,
        String message,
        ReadMessageRange range,
        Long unreadCount,
        LocalDateTime timeStamp,
        MessageType messageType
) {
    public static ChatMessage from(ChattingMessage chattingMessage) {
        return ChatMessage.builder()
                .messageId(chattingMessage.getId())
                .roomId(chattingMessage.getRoomId())
                .senderId(chattingMessage.getSenderId())
                .senderName(chattingMessage.getSenderName())
                .message(chattingMessage.getMessage())
                .unreadCount(chattingMessage.getUnreadCount())
                .timeStamp(chattingMessage.getTimeStamp())
                .messageType(chattingMessage.getMessageType())
                .build();
    }

    public static ChatMessage of(long roomId, Long senderId, String senderName, ReadMessageRange range, MessageType messageType) {
        return ChatMessage.builder()
                .roomId(roomId)
                .senderId(senderId)
                .senderName(senderName)
                .range(range)
                .timeStamp(LocalDateTime.now())
                .messageType(messageType)
                .build();
    }
}
