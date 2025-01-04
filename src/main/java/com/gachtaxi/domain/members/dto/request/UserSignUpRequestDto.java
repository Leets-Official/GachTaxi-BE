package com.gachtaxi.domain.members.dto.request;


import com.gachtaxi.domain.members.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSignUpRequestDto(
        @NotBlank String email,
        @NotBlank String nickName,
        @NotBlank String realName,
        @NotBlank Long studentNumber,
        @NotBlank Gender gender,
        @NotNull Boolean termsAgreement,
        @NotNull Boolean privacyAgreement,
        @NotNull Boolean marketingAgreement,
        @NotNull Boolean twoFactorAuthentication,
        Long kakaoId,
        Long googleId
){}
