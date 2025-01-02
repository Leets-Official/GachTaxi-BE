package com.gachtaxi.domain.members.service;

import com.gachtaxi.global.auth.kakao.util.KakaoUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.*;

/*
* AuthService는 인증 로직 책임을 가진다.
* */

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoUtil kakaoUtil;
    private final MemberService memberService;

    public String kakaoLogin(String authCode, HttpServletResponse response) {
        // 인가 코드로 토큰 발급
        KakaoAccessToken kakaoAccessToken = kakaoUtil.reqeustKakaoToken(authCode);

        // 토큰으로 사용자 정보(email) 가져오기
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoUtil.requestKakaoProfile(kakaoAccessToken.access_token());


        return kakaoAccessToken.access_token();
    }
}
