package com.gachtaxi.domain.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoticeCreateRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        @NotBlank
        String content

) {}
