package com.gachtaxi.global.auth.dto;

import com.gachtaxi.global.auth.enums.OauthLoginStatus;
import lombok.Builder;

/*
* 카카오와 구글 로그인 시 사용하는 Response
* */

public class OauthLoginResponse {

    @Builder
    public record oauthKakaoResponse(
            Long userId,
            Long kakaoId,
            OauthLoginStatus status
    ){}

    /*
    * public record googleLoginResponse(){}
    * */

}
