package com.gachtaxi.domain.members.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InactiveMemberAuthCodeRequestDto(
        @NotBlank String email,
        @NotBlank String authCode
) { }
