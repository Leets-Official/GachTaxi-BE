package com.gachtaxi.global.auth.kakao.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.*;

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

    private final RestClient restClient = RestClient.create();

    public KakaoAccessToken reqeustKakaoToken(String authCode){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClient);
        params.add("redirect_url", kakaoRedirect);
        params.add("code", authCode);

        return restClient.post()
                .uri(kakaoTokenUri)
                .body(params)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .body(KakaoAccessToken.class);
    }
}
