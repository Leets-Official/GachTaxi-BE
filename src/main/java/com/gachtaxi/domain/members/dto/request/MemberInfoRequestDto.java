package com.gachtaxi.domain.members.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberInfoRequestDto(
        @NotNull String profilePicture,
        @NotNull String nickName,
        @NotNull String accountNumber
) {
}
