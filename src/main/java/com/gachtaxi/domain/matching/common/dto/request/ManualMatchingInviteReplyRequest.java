package com.gachtaxi.domain.matching.common.dto.request;

import com.gachtaxi.domain.matching.common.entity.enums.MatchingInviteStatus;
import jakarta.validation.constraints.NotNull;

public record ManualMatchingInviteReplyRequest(
        @NotNull
        Long matchingRoomId,
        @NotNull
        String notificationId,
        @NotNull
        MatchingInviteStatus status
        ) {
}
