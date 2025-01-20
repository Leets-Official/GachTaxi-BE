package com.gachtaxi.global.auth.jwt.dto;

import lombok.Builder;

@Builder
public record JwtTokenDto(
        String accessToken,
        String refreshToken
){
    public static JwtTokenDto of(String accessToken, String refreshToken) {
        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
    }

    public static JwtTokenDto of(String accessToken) {
        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    public boolean isTemporaryUser(){
        return this.refreshToken == null || this.refreshToken.isEmpty();
    }
}
