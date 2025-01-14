package com.gachtaxi.domain.members.dto.request;

import com.gachtaxi.domain.members.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberSupplmentRequestDto(
        String profilePicture,
        @NotBlank String nickname,
        @NotBlank String realName,
        @NotNull Long studentNumber,
        @NotNull Gender gender
) {
}
