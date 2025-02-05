package com.gachtaxi.domain.notification.entity;

import com.gachtaxi.domain.notification.entity.enums.NotificationStatus;
import com.gachtaxi.domain.notification.entity.enums.NotificationType;
import com.gachtaxi.domain.notification.entity.payload.NotificationPayload;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import static com.gachtaxi.domain.notification.entity.enums.NotificationStatus.*;

@Getter
@Document(collection = "notifications")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    private String id;

    private Long receiverId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(columnDefinition = "text")
    private String content;

    private NotificationPayload payload;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = UNREAD;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    private LocalDateTime readAt;

    public void read() {
        this.status = READ;
        this.readAt = LocalDateTime.now();
    }

    public void failToSend() {
        this.status = UNSENT;
    }

    public static Notification of(Long receiverId, NotificationType type, String content, NotificationPayload payload) {
        return Notification.builder()
                .receiverId(receiverId)
                .type(type)
                .content(content)
                .payload(payload)
                .build();
    }
}
