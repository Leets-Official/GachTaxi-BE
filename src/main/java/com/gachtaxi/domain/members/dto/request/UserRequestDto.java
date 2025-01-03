package com.gachtaxi.domain.members.dto.request;


import com.gachtaxi.domain.members.entity.enums.Gender;

public class UserRequestDto {

    public record registerDto(
            //String profilePicture,
            String email,
            String nickName,
            String realName,
            String studentNumber,
            //String phoneNumber,
            Gender gender,
            Boolean termsAgreement,
            Boolean privacyAgreement,
            Boolean marketingAgreement,
            Boolean twoFactorAuthentication,
            Long kakaoId,
            Long googleId
    ){}
}
