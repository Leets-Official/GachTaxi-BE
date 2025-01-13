package com.gachtaxi.domain.chat.entity;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import jakarta.persistence.Id;
import lombok.*;
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

    private LocalDateTime timeStamp;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static ChattingMessage from(ChatMessage chatMessage) {
        return ChattingMessage.builder()
                .senderId(chatMessage.senderId())
                .senderName(chatMessage.senderName())
                .roomId(chatMessage.roomId())
                .message(chatMessage.message())
                .timeStamp(chatMessage.timeStamp())
                .build();
    }
}
