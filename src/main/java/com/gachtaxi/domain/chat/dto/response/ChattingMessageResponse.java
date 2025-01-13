package com.gachtaxi.domain.chat.dto.response;

import com.gachtaxi.domain.chat.entity.ChattingMessage;
import com.gachtaxi.domain.chat.entity.enums.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChattingMessageResponse(
        String senderName,
        String message,
        LocalDateTime timeStamp,
        MessageType messageType
) {
    public static ChattingMessageResponse from(ChattingMessage chattingMessage) {
        return ChattingMessageResponse.builder()
                .senderName(chattingMessage.getSenderName())
                .message(chattingMessage.getMessage())
                .timeStamp(chattingMessage.getTimeStamp())
                .messageType(chattingMessage.getMessageType())
                .build();
    }
}
