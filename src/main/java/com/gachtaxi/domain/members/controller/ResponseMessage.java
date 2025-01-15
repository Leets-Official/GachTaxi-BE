package com.gachtaxi.domain.members.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
    // MemberController
    REGISTER_SUCCESS("회원가입에 성공했습니다."),

    // AuthController
    KAKAO_INTEGRATE("기존 구글 회원가입, 카카오 계정으로 통합 로그인합니다."),
    GOOGLE_INTEGRATE("기존 카카오 회원가입, 구글 계정으로 통합 로그인합니다."),
    REFRESH_TOKEN_REISSUE("토큰 재발급에 성공했습니다."),
    LOGIN_SUCCESS("로그인 성공에 성공했습니다."),
    UN_REGISTER("회원가입을 진행해주세요");

    private final String message;
}
