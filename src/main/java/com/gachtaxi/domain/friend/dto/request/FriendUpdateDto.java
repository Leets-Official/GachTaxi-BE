package com.gachtaxi.domain.friend.dto.request;

import com.gachtaxi.domain.friend.entity.enums.FriendStatus;
import jakarta.validation.constraints.NotNull;

public record FriendUpdateDto(
        @NotNull Long memberId,
        @NotNull String notificationId,
        @NotNull FriendStatus status
) {
}
