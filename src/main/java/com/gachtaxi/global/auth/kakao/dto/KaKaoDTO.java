package com.gachtaxi.global.auth.kakao.dto;

import com.gachtaxi.global.auth.enums.OauthLoginStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class KaKaoDTO {

    public record KakaoAuthCode(
            @NotBlank String authCode
    ){}

    public record KakaoAccessToken(
            String access_token,
            String token_type,
            String refresh_token,
            int expires_in,
            String scope,
            int refresh_token_expires_in
    ) {}

    public record KakaoUserInfoResponse(
            Long id,
            KakaoAccount kakao_account
    ) {}

    public record KakaoAccount(
            Boolean is_email_valid,
            Boolean is_email_verified,
            String email,
            Profile profile
    ) {}

    public record Profile(
            String nickname,
            Boolean is_default_nickname
    ) {}

    @Builder
    public record OauthKakaoResponse(
            Long userId,
            OauthLoginStatus status
    ){}

}
