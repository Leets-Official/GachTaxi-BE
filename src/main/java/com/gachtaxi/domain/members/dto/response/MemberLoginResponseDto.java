package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.global.auth.jwt.dto.JwtTokenDto;
import lombok.Builder;

import static com.gachtaxi.domain.members.controller.ResponseMessage.*;

@Builder
public record MemberLoginResponseDto(
        String status,
        MemberResponseDto memberResponseDto,
        String authorization,
        String refreshToken
) {
    public static MemberLoginResponseDto from(MemberResponseDto memberResponseDto, JwtTokenDto jwtTokenDto) {
        return MemberLoginResponseDto.builder()
                .status(LOGIN_SUCCESS.name())
                .memberResponseDto(memberResponseDto)
                .authorization(jwtTokenDto.accessToken())
                .refreshToken(jwtTokenDto.refreshToken())
                .build();
    }

    public static MemberLoginResponseDto from() {
        return MemberLoginResponseDto.builder()
                .status(UN_REGISTER.name())
                .build();
    }
}
