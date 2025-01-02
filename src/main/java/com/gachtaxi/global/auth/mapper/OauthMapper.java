package com.gachtaxi.global.auth.mapper;

import org.springframework.stereotype.Component;

import static com.gachtaxi.global.auth.enums.OauthLoginStatus.*;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.*;

@Component
public class OauthMapper {

    public oauthKakaoResponse toKakaoUnRegisterResponse(KakaoUserInfoResponse userInfo) {
        return oauthKakaoResponse.builder()
                .kakaoId(userInfo.id())
                .status(UN_REGISTER)
                .build();
    }

    // jwt 토큰 추가 할 것.
    public oauthKakaoResponse toKakaoLoginResponse(KakaoUserInfoResponse userInfo, Long userId) {
        return oauthKakaoResponse.builder()
                .userId(userId)
                .kakaoId(userInfo.id())
                .status(LOGIN)
                .build();
    }


}
