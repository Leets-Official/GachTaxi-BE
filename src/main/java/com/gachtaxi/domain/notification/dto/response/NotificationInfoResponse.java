package com.gachtaxi.domain.notification.dto.response;

public record NotificationInfoResponse(
        int unreadCount,
        boolean hasUnreadNotifications
) {
}
