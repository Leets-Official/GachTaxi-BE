package com.gachtaxi.domain.chat.entity;

import com.gachtaxi.domain.chat.dto.request.ChatMessageRequest;
import com.gachtaxi.domain.chat.entity.enums.MessageType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chatting_messages")
public class ChattingMessage {

    @Id
    private String id;

    private Long senderId;

    private String senderName;

    private Long roomId;

    private String message;

    private Long unreadCount;

    private MessageType messageType;

    private LocalDateTime timeStamp;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static ChattingMessage of(ChatMessageRequest request, long roomId, long senderId, String senderName, long unreadCount) {
        return ChattingMessage.builder()
                .senderId(senderId)
                .senderName(senderName)
                .roomId(roomId)
                .message(request.message())
                .unreadCount(unreadCount)
                .messageType(MessageType.MESSAGE)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    public static ChattingMessage of(long roomId, Long senderId, String senderName, String message, MessageType messageType) {
        return ChattingMessage.builder()
                .senderId(senderId)
                .senderName(senderName)
                .roomId(roomId)
                .message(message)
                .messageType(messageType)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
