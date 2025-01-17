package com.gachtaxi.domain.notification.dto.response;

import com.gachtaxi.domain.notification.entity.Notification;
import com.gachtaxi.domain.notification.entity.enums.NotificationStatus;
import com.gachtaxi.domain.notification.entity.enums.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        long notificationId,
        long senderId,
        long receiverId,
        NotificationType type,
        NotificationStatus status,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getId())
                .senderId(notification.getSenderId())
                .receiverId(notification.getReceiverId())
                .type(notification.getType())
                .status(notification.getStatus())
                .title(notification.getTitle())
                .content(notification.getContent())
                .createdAt(notification.getCreateDate())
                .build();
    }
}
