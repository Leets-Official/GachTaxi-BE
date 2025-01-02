package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.auth.jwt.service.JwtService;
import com.gachtaxi.global.auth.kakao.util.KakaoUtil;
import com.gachtaxi.global.auth.mapper.OauthMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.*;
import static com.gachtaxi.global.auth.dto.OauthLoginResponse.*;


/*
* AuthService는 인증 로직 책임을 가진다.
* */

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoUtil kakaoUtil;
    private final OauthMapper oauthMapper;
    private final JwtService jwtService;
    private final MemberService memberService;

    public oauthKakaoResponse kakaoLogin(String authCode, HttpServletResponse response) {
        KakaoAccessToken kakaoAccessToken = kakaoUtil.reqeustKakaoToken(authCode);
        KakaoUserInfoResponse userInfo = kakaoUtil.requestKakaoProfile(kakaoAccessToken.access_token());

        String email = userInfo.kakao_account().email();
        Optional<Members> optionalMember = memberService.findByEmail(email);

        if(optionalMember.isEmpty()) {
            return oauthMapper.toKakaoUnRegisterResponse(userInfo);
        }

        Members member = optionalMember.get();
        jwtService.responseJwtToken(member.getId(), email, member.getRole(), response);

        return oauthMapper.toKakaoLoginResponse(userInfo, member.getId());
    }
}
