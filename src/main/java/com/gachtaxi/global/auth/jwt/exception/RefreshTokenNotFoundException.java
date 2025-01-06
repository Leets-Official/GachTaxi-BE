package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.REDIS_NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class RefreshTokenNotFoundException extends BaseException {
    public RefreshTokenNotFoundException() {
        super(UNAUTHORIZED, REDIS_NOT_FOUND.getMessage());
    }
}
