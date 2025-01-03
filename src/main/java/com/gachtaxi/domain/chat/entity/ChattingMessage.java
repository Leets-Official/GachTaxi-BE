package com.gachtaxi.domain.chat.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document(collection = "chatting_messages")
public class ChattingMessage {

    @Id
    private String id;

    private Long senderId;

    private String nickName;

    private Long roomId;

    private String message;

    private LocalDateTime timeStamp;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
