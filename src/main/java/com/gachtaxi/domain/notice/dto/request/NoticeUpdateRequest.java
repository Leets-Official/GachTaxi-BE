package com.gachtaxi.domain.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoticeUpdateRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        @NotBlank
        String content

) {}
