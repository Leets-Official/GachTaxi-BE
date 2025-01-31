package com.gachtaxi.domain.notification.entity.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequestPayload extends NotificationPayload {

//    @Builder.Default
//    private FriendStatus status = FriendStatus.PENDING;

    private Long senderId;

    public static FriendRequestPayload from(Long senderId) {
        return FriendRequestPayload.builder().senderId(senderId).build();
    }
}
