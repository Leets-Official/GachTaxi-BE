package com.gachtaxi.global.common.mail.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.global.common.mail.message.ErrorMessage.AUTH_CODE_EXPIRED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class AuthCodeExpirationException extends BaseException {
    public AuthCodeExpirationException() {
        super(BAD_REQUEST, AUTH_CODE_EXPIRED.getMessage());
    }
}
