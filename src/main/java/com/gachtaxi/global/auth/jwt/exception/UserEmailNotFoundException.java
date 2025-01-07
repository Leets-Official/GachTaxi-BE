package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.USER_NOT_FOUND_EMAIL;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UserEmailNotFoundException extends BaseException {
    public UserEmailNotFoundException() {
        super(UNAUTHORIZED, USER_NOT_FOUND_EMAIL.getMessage());
    }
}
