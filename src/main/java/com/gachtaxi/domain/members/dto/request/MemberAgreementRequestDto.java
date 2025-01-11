package com.gachtaxi.domain.members.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberAgreementRequestDto(
        @NotNull boolean termsAgreement,
        @NotNull boolean privacyAgreement,
        @NotNull boolean marketingAgreement
) {
}
