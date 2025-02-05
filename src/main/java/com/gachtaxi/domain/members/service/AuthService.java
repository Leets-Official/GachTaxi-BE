package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.request.InactiveMemberDto;
import com.gachtaxi.domain.members.dto.request.MemberTokenDto;
import com.gachtaxi.domain.members.dto.response.LoginDto;
import com.gachtaxi.domain.members.dto.response.MemberResponseDto;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.auth.google.dto.GoogleTokenResponse;
import com.gachtaxi.global.auth.google.dto.GoogleUserInfoResponse;
import com.gachtaxi.global.auth.google.utils.GoogleUtils;
import com.gachtaxi.global.auth.jwt.service.JwtService;
import com.gachtaxi.global.auth.kakao.util.KakaoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.gachtaxi.domain.members.entity.enums.UserStatus.INACTIVE;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.KakaoAccessToken;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.KakaoUserInfoResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoUtil kakaoUtil;
    private final GoogleUtils googleUtils;
    private final JwtService jwtService;
    private final MemberService memberService;

    public LoginDto kakaoLogin(String authCode) {
        KakaoUserInfoResponse userInfo = getKakaoUserInfoResponse(authCode);
        Long kakaoId = userInfo.id();

        Optional<Members> optionalMember = memberService.findByKakaoId(kakaoId);
        if(optionalMember.isEmpty()) {
            return LoginDto.from(
                    jwtService.generateTmpAccessToken(memberService.saveTmpKakaoMember(kakaoId))
            );
        }

        Members members = optionalMember.get();
        if(members.getStatus() == INACTIVE){
            return LoginDto.from(
                    jwtService.generateTmpAccessToken(InactiveMemberDto.of(optionalMember.get()))
            );
        }

        return LoginDto.of(
                jwtService.generateJwtToken(MemberTokenDto.from(members)),
                MemberResponseDto.from(members)
        );
    }

    public LoginDto googleLogin(String authCode) {
        GoogleUserInfoResponse userInfo = getGoogleUserInfoResponse(authCode);
        String googleId = userInfo.id();

        Optional<Members> optionalMember = memberService.findByGoogleId(googleId);
        if(optionalMember.isEmpty()) {
            return LoginDto.from(
                    jwtService.generateTmpAccessToken(memberService.saveTmpGoogleMember(googleId))
            );
        }

        Members members = optionalMember.get();
        if(members.getStatus() == INACTIVE){
            return LoginDto.from(
                    jwtService.generateTmpAccessToken(InactiveMemberDto.of(optionalMember.get()))
            );
        }

        return LoginDto.of(
                jwtService.generateJwtToken(MemberTokenDto.from(members)),
                MemberResponseDto.from(members)
        );
    }


    /*
    * refactoring
    * */

    private KakaoUserInfoResponse getKakaoUserInfoResponse(String authCode) {
        KakaoAccessToken kakaoAccessToken = kakaoUtil.reqeustKakaoToken(authCode);
        KakaoUserInfoResponse userInfo = kakaoUtil.requestKakaoProfile(kakaoAccessToken.access_token());
        return userInfo;
    }

    private GoogleUserInfoResponse getGoogleUserInfoResponse(String authCode) {
        GoogleTokenResponse googleAccessToken = googleUtils.reqeustGoogleToken(authCode);
        GoogleUserInfoResponse userInfo = googleUtils.requestGoogleProfile(googleAccessToken.access_token());
        return userInfo;
    }

}
