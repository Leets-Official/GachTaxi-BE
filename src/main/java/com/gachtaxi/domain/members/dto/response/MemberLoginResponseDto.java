package com.gachtaxi.domain.members.dto.response;

import lombok.Builder;

import static com.gachtaxi.domain.members.controller.ResponseMessage.*;

@Builder
public record MemberLoginResponseDto(
        String status,
        MemberResponseDto memberResponseDto
) {
    public static MemberLoginResponseDto from(MemberResponseDto memberResponseDto) {
        return MemberLoginResponseDto.builder()
                .status(LOGIN_SUCCESS.name())
                .memberResponseDto(memberResponseDto)
                .build();
    }

    public static MemberLoginResponseDto from() {
        return MemberLoginResponseDto.builder()
                .status(UN_REGISTER.name())
                .build();
    }
}
