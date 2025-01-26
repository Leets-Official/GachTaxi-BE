package com.gachtaxi.domain.chat.dto.response;

import com.gachtaxi.domain.chat.entity.ChattingMessage;
import com.gachtaxi.domain.chat.entity.enums.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChattingMessageResponse(
        String messageId,
        Long senderId,
        String senderName,
        String message,
        Long unreadCount,
        LocalDateTime timeStamp,
        MessageType messageType
) {
    public static ChattingMessageResponse from(ChattingMessage chattingMessage) {
        return ChattingMessageResponse.builder()
                .messageId(chattingMessage.getId())
                .senderId(chattingMessage.getSenderId())
                .senderName(chattingMessage.getSenderName())
                .message(chattingMessage.getMessage())
                .unreadCount(chattingMessage.getUnreadCount())
                .timeStamp(chattingMessage.getTimeStamp())
                .messageType(chattingMessage.getMessageType())
                .build();
    }
}
