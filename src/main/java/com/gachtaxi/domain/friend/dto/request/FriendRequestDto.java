package com.gachtaxi.domain.friend.dto.request;

import jakarta.validation.constraints.NotNull;

public record FriendRequestDto(
        @NotNull Long memberId
) {
}
