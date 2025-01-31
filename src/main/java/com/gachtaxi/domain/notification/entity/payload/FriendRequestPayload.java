package com.gachtaxi.domain.notification.entity.payload;

import com.gachtaxi.domain.notification.entity.enums.NotificationStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequestPayload extends NotificationPayload {

    @Builder.Default
    private NotificationStatus notificationStatus = NotificationStatus.PENDING;

    private Long senderId;
}
