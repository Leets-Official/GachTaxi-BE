package com.gachtaxi.domain.notification.dto.response;

import com.gachtaxi.domain.notification.entity.Notification;
import com.gachtaxi.domain.notification.entity.enums.NotificationType;
import com.gachtaxi.domain.notification.entity.payload.NotificationPayload;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        String notificationId,
        long receiverId,
        NotificationType type,
        String content,
        NotificationPayload payload,
        LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getId())
                .receiverId(notification.getReceiverId())
                .type(notification.getType())
                .content(notification.getContent())
                .payload(notification.getPayload())
                .createdAt(notification.getCreateDate())
                .build();
    }
}
