package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.global.common.mail.dto.enums.EmailStatus;

public record MemberMailResponseDto(
        EmailStatus status,
        String email,
        Long kakaoId,
        String googleId
) {
}
