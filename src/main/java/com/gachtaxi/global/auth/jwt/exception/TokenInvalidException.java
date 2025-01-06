package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.JWT_TOKEN_INVALID;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class TokenInvalidException extends BaseException {
    public TokenInvalidException() {
        super(UNAUTHORIZED, JWT_TOKEN_INVALID.getMessage());
    }
}
