package com.gachtaxi.domain.members.dto.request;


import com.gachtaxi.domain.members.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;

public class UserRequestDto {

    public record SignUpDto(
            //String profilePicture,
            @NotBlank String email,
            @NotBlank String nickName,
            @NotBlank String realName,
            @NotBlank String studentNumber,
            //String phoneNumber,
            @NotBlank Gender gender,
            Boolean termsAgreement,
            Boolean privacyAgreement,
            Boolean marketingAgreement,
            Boolean twoFactorAuthentication,
            Long kakaoId,
            Long googleId
    ){}
}
