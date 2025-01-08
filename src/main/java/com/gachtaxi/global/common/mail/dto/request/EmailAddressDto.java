package com.gachtaxi.global.common.mail.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmailAddressDto(
       @NotBlank String email
) {
}
