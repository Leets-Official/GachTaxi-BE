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
    private Long matchingRoomId;
    private String profilePicture;

    public static MatchingInvitePayload from(String senderNickname, String profilePicture, Long matchingRoomId) {
        return MatchingInvitePayload.builder()
                .senderNickname(senderNickname)
                .profilePicture(profilePicture)
                .matchingRoomId(matchingRoomId)
                .build();
    }
}
