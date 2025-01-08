package com.gachtaxi.domain.members.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.gachtaxi.global.common.mail.message.ErrorMessage.AUTH_CODE_NOT_MATCH;


public class AuthCodeNotMatchException extends BaseException {
    public AuthCodeNotMatchException() {
        super(HttpStatus.BAD_REQUEST, AUTH_CODE_NOT_MATCH.getMessage());
    }
}
