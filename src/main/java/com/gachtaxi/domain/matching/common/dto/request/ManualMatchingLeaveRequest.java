package com.gachtaxi.domain.matching.common.dto.request;

import jakarta.validation.constraints.NotNull;

public record ManualMatchingLeaveRequest(
        @NotNull Long userId,
        @NotNull Long roomId
) {
}
