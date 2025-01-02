package com.gachtaxi.global.auth.kakao.dto;

public class KaKaoDTO {

    public record KakaoAccessToken(
            String access_token,
            String token_type,
            String refresh_token,
            int expires_in,
            String scope,
            int refresh_token_expires_in
    ) {}
}
