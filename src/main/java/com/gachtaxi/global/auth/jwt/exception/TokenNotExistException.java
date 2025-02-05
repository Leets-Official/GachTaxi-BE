package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.JWT_TOKEN_NOT_EXIST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class TokenNotExistException extends BaseException {
    public TokenNotExistException () {
        super(UNAUTHORIZED, JWT_TOKEN_NOT_EXIST.getMessage());
    }
}
