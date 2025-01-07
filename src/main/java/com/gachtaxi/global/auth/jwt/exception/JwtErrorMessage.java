package com.gachtaxi.global.auth.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorMessage {

    JWT_TOKEN_UN_VALID("유효하지 않은 토큰 입니다."),
    JWT_TOKEN_NOT_EXIST("헤더에 인증 토큰이 존재하지 않습니다");

    private final String message;
}
