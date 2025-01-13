package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.COOKIE_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CookieNotFoundException extends BaseException {
    public CookieNotFoundException() {
        super(BAD_REQUEST, COOKIE_NOT_FOUND.getMessage());
    }
}
