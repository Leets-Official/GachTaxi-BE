package com.gachtaxi.domain.members.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AccountPostRequest(
    @NotBlank
    String accountNumber
) {

}
