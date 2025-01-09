package com.gachtaxi.global.common.mail.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.global.common.mail.message.ErrorMessage.AUTH_CODE_NOT_MATCH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


public class AuthCodeNotMatchException extends BaseException {
    public AuthCodeNotMatchException() {
        super(BAD_REQUEST, AUTH_CODE_NOT_MATCH.getMessage());
    }
}
