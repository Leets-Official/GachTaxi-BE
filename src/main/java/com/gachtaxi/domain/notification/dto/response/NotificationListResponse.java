package com.gachtaxi.domain.notification.dto.response;

import java.util.List;

public record NotificationListResponse(
        List<NotificationResponse> response,
        NotificationPageableResponse pageable
) {
    public static NotificationListResponse of(List<NotificationResponse> response, NotificationPageableResponse pageable) {
        return new NotificationListResponse(response, pageable);
    }
}
