package com.gachtaxi.global.auth.google.dto;

public record GoogleTokenResponse(
        String access_token,
        String expires_in,
        String scope,
        String token_type,
        String id_token
) {
}