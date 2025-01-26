package com.gachtaxi.domain.notification.dto.response;

public record NotificationInfoResponse(
        int unreadCount,
        boolean hasUnreadNotifications
) {
    public static NotificationInfoResponse of(int unreadCount, boolean hasUnreadNotifications) {
        return new NotificationInfoResponse(unreadCount, hasUnreadNotifications);
    }
}
