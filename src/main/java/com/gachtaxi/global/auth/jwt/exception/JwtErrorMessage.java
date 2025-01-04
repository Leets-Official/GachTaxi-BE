package com.gachtaxi.global.auth.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorMessage {

    JWT_TOKEN_UN_VALID("유효하지 않은 토큰 입니다.");

    private final String message;
}
