package com.gachtaxi.domain.members.dto.request;

public record MemberIntegrationRequestDto(
        String email,
        String authCode,
        Long kakaoId,
        String googleId
) {
}
