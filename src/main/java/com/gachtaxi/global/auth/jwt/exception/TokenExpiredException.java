package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.JWT_TOKEN_EXPIRED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException() {
        super(UNAUTHORIZED, JWT_TOKEN_EXPIRED.getMessage());
    }
}

