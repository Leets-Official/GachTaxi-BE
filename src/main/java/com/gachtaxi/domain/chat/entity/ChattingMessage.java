package com.gachtaxi.domain.chat.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
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
}
