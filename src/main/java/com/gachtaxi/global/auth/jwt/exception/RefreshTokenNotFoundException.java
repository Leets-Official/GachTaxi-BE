package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.REDIS_NOT_FOUND;

public class RefreshTokenNotFoundException extends BaseException {
    public RefreshTokenNotFoundException() {
        super(HttpStatus.UNAUTHORIZED, REDIS_NOT_FOUND.getMessage());
    }
}
