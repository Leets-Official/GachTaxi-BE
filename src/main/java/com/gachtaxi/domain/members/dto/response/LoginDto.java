package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.global.auth.jwt.dto.JwtTokenDto;
import lombok.Builder;

@Builder
public record LoginDto(
        JwtTokenDto jwtTokenDto,
        MemberResponseDto memberResponseDto
) {
    public static LoginDto of(JwtTokenDto jwtTokenDto, MemberResponseDto memberResponseDto) {
        return LoginDto.builder()
                .jwtTokenDto(jwtTokenDto)
                .memberResponseDto(memberResponseDto)
                .build();
    }

    public static LoginDto from(JwtTokenDto jwtTokenDto) {
        return LoginDto.builder()
                .jwtTokenDto(jwtTokenDto)
                .build();
    }

    public boolean isTemporaryUser(){
        return this.memberResponseDto == null;
    }
}
