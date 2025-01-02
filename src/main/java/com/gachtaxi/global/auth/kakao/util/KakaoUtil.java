package com.gachtaxi.global.auth.kakao.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/*
* KakaoUtil은 외부 API 통신 및 카카오 관련 작업을 담당(책임)한다.
* */

@Slf4j
@Component
public class KakaoUtil {

    @Value("${gachtaxi.auth.kakao.client}")
    private String kakaoClient;

    @Value("${gachtaxi.auth.kakao.redirect}")
    private String kakaoRedirect;

    @Value("${gachtaxi.auth.kakao.token_uri}")
    private String kakaoTokenUri;

    @Value("${gachtaxi.auth.kakao.user_profile}")
    private String kakaoUserProfileUri;
}
