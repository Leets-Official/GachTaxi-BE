package com.gachtaxi.domain.notification.entity.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInvitePayload extends NotificationPayload {
    private String senderNickname;

    public static MatchingInvitePayload from(String senderNickname) {
        return MatchingInvitePayload.builder()
                .senderNickname(senderNickname)
                .build();
    }
}
