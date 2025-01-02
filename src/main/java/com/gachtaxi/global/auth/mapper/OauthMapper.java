package com.gachtaxi.global.auth.mapper;

import org.springframework.stereotype.Component;

import static com.gachtaxi.global.auth.enums.OauthLoginStatus.*;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.*;
import static com.gachtaxi.global.auth.dto.OauthLoginResponse.*;

@Component
public class OauthMapper {

    public kakaoLoginResponse toKakaoUnRegisterResponse(KakaoUserInfoResponse userInfo) {
        return kakaoLoginResponse.builder()
                .kakaoId(userInfo.id())
                .status(UN_REGISTER)
                .build();
    }

    // jwt 토큰 추가 할 것.
    public kakaoLoginResponse toKakaoLoginResponse(KakaoUserInfoResponse userInfo, Long userId) {
        return kakaoLoginResponse.builder()
                .userId(userId)
                .kakaoId(userInfo.id())
                .status(LOGIN)
                .build();
    }


}
