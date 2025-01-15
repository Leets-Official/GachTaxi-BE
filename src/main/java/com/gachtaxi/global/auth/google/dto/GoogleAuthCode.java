package com.gachtaxi.global.auth.google.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleAuthCode(
        @NotBlank String authCode
) {
}
