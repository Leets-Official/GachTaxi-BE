package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.request.InactiveMemberDto;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.auth.jwt.dto.JwtTokenDto;
import com.gachtaxi.global.auth.jwt.service.JwtService;
import com.gachtaxi.global.auth.kakao.util.KakaoUtil;
//import com.gachtaxi.global.auth.mapper.OauthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.gachtaxi.domain.members.entity.enums.UserStatus.INACTIVE;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.*;


/*
* AuthService는 인증 로직 책임을 가진다.
* */


@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoUtil kakaoUtil;
    private final JwtService jwtService;
    private final MemberService memberService;

    public JwtTokenDto kakaoLogin(String authCode) {
        KakaoAccessToken kakaoAccessToken = kakaoUtil.reqeustKakaoToken(authCode);
        KakaoUserInfoResponse userInfo = kakaoUtil.requestKakaoProfile(kakaoAccessToken.access_token());

        Long kakaoId = userInfo.id();
        Optional<Members> optionalMember = memberService.findByKakaoId(kakaoId);

        if(optionalMember.isEmpty()) {
            InactiveMemberDto tmpDto = memberService.saveTmpMember(kakaoId);
            return jwtService.generateTmpAccessToken(tmpDto);
        }

        // 회원 가입 진행 중 중단된 유저 또한 다시 임시 토큰을 재발급해준다.
        Members member = optionalMember.get();
        if(member.getStatus() == INACTIVE){
            InactiveMemberDto tmpDto = InactiveMemberDto.of(optionalMember.get());
            return jwtService.generateTmpAccessToken(tmpDto);
        }

        return jwtService.generateJwtToken(member.getId(), member.getEmail(), member.getRole().name());
    }
}
