package com.gachtaxi.global.auth.mapper;

import org.springframework.stereotype.Component;

import static com.gachtaxi.global.auth.enums.OauthLoginStatus.*;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.*;

@Component
public class OauthMapper {

    public OauthKakaoResponse toKakaoUnRegisterResponse(Long userId) {
        return OauthKakaoResponse.builder()
                .userId(userId)
                .status(UN_REGISTER)
                .build();
    }

    // jwt 토큰 추가 할 것.
    public OauthKakaoResponse toKakaoLoginResponse(Long userId) {
        return OauthKakaoResponse.builder()
                .userId(userId)
                .status(LOGIN)
                .build();
    }


}
