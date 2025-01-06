package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.JWT_TOKEN_INVALID;

public class TokenInvalidException extends BaseException {
    public TokenInvalidException() {
        super(HttpStatus.UNAUTHORIZED, JWT_TOKEN_INVALID.getMessage());
    }
}
