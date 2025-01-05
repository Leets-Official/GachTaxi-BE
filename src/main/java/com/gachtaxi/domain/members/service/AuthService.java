package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.auth.jwt.service.JwtService;
import com.gachtaxi.global.auth.kakao.util.KakaoUtil;
import com.gachtaxi.global.auth.mapper.OauthMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.*;


/*
* AuthService는 인증 로직 책임을 가진다.
* */


@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoUtil kakaoUtil;
    private final OauthMapper oauthMapper;
    private final JwtService jwtService;
    private final MemberService memberService;

    public OauthKakaoResponse kakaoLogin(String authCode, HttpServletResponse response) {
        KakaoAccessToken kakaoAccessToken = kakaoUtil.reqeustKakaoToken(authCode);
        KakaoUserInfoResponse userInfo = kakaoUtil.requestKakaoProfile(kakaoAccessToken.access_token());

        Long kakaoId = userInfo.id();
        Optional<Members> optionalMember = memberService.findByKakaoId(kakaoId);

        if(optionalMember.isEmpty()) {
            return oauthMapper.toKakaoUnRegisterResponse(userInfo);
        }

        Members member = optionalMember.get();
        jwtService.responseJwtToken(member.getId(), member.getEmail(), member.getRole(), response);
        return oauthMapper.toKakaoLoginResponse(userInfo, member.getId());
    }
}
