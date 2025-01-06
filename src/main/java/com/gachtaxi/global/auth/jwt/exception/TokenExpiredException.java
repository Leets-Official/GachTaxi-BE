package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.JWT_TOKEN_EXPIRED;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException() {
        super(HttpStatus.UNAUTHORIZED, JWT_TOKEN_EXPIRED.getMessage());
    }
}

