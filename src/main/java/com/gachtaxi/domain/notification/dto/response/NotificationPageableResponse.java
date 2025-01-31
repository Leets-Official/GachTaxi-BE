package com.gachtaxi.domain.notification.dto.response;

import com.gachtaxi.domain.notification.entity.Notification;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record NotificationPageableResponse(
        int pageNumber,
        int pageSize,
        int numberOfElements,
        boolean last
) {
    public static NotificationPageableResponse from(Slice<Notification> slice) {
        return NotificationPageableResponse.builder()
                .pageNumber(slice.getNumber())
                .pageSize(slice.getSize())
                .numberOfElements(slice.getNumberOfElements())
                .last(slice.isLast())
                .build();
    }
}
