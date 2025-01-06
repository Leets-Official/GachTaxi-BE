package com.gachtaxi.global.auth.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorMessage {

    JWT_TOKEN_FORBIDDEN("권한이 없습니다."),
    USER_NOT_FOUND_EMAIL("해당 이메일의 유저를 찾을 수 없습니다"),
    JWT_TOKEN_NOT_FOUND("토큰을 찾을 수 없습니다"),
    JWT_TOKEN_EXPIRED("만료된 토큰입니다."),
    JWT_TOKEN_INVALID("유효하지 않은 토큰 입니다.");

    private final String message;
}
