package com.gachtaxi.domain.members.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
    // MemberController
    REGISTER_SUCCESS("회원가입에 성공했습니다."),
    MEMBER_INFO_UPDATE("유저 정보를 성공적으로 수정했습니다!"),
    MEMBER_INFO_RESPONSE("유저 정보를 반환합니다."),
    FCM_TOKEN_UPDATE_SUCCESS("FCM 토큰 업데이트에 성공했습니다."),

    // AuthController
    ALREADY_SIGN_UP("이미 가입된 이메일 입니다!"),
    REFRESH_TOKEN_REISSUE("토큰 재발급에 성공했습니다."),
    LOGIN_SUCCESS("로그인 성공에 성공했습니다."),
    UN_REGISTER("회원가입을 진행해주세요"),

    // BlacklistController
    BLACKLIST_SAVE_SUCCESS("블랙리스트 등록에 성공했습니다."),
    BLACKLIST_DELETE_SUCCESS("블랙리스트 삭제에 성공했습니다."),
    BLACKLIST_FIND_ALL_SUCCESS("블랙리스트 조회에 성공했습니다."),

    // AccountController
    ACCOUNT_GET_SUCCESS("계좌 정보를 성공적으로 가져왔습니다."),
    ACCOUNT_UPDATE_SUCCESS("계좌 정보를 성공적으로 수정했습니다.");

    private final String message;
}
