package com.gachtaxi.domain.notification.entity;

import com.gachtaxi.domain.notification.entity.enums.NotificationStatus;
import com.gachtaxi.domain.notification.entity.enums.NotificationType;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.gachtaxi.domain.notification.entity.enums.NotificationStatus.*;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    private Long senderId;

    private Long receiverId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = UNREAD;

    private LocalDateTime readAt;

    public static Notification of(Long senderId, Long receiverId, NotificationType type, String content) {
        return Notification.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .type(type)
                .content(content)
                .build();
    }

    public static Notification of(Long receiverId, NotificationType type, String content) {
        return Notification.builder()
                .receiverId(receiverId)
                .type(type)
                .content(content)
                .build();
    }

    public void read() {
        this.status = READ;
        this.readAt = LocalDateTime.now();
    }

    public void failToSend() {
        this.status = UNSENT;
    }
}
