package com.gachtaxi.domain.notification.entity.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingPayload extends NotificationPayload {
    private String startLocationName;

    private String endLocationName;

    public static MatchingPayload of(String startLocationName, String endLocationName) {
        return MatchingPayload.builder()
                .startLocationName(startLocationName)
                .endLocationName(endLocationName)
                .build();
    }
}
