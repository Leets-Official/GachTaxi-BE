package com.gachtaxi.domain.members.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.members.exception.ErrorMessage.EMAIL_FROM_INVALID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EmailFormInvalidException extends BaseException {
    public EmailFormInvalidException() {
        super(BAD_REQUEST, EMAIL_FROM_INVALID.getMessage());
    }
}
