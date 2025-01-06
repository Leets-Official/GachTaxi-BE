package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.COOKIE_NOT_FOUND;

public class CookieNotFoundException extends BaseException {
    public CookieNotFoundException() {
        super(HttpStatus.BAD_REQUEST, COOKIE_NOT_FOUND.getMessage());
    }
}
