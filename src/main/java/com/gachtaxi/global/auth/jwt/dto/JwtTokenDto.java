package com.gachtaxi.global.auth.jwt.dto;

import lombok.Builder;

@Builder
public record JwtTokenDto(
        String accessToken,
        String refreshToken
){
}
