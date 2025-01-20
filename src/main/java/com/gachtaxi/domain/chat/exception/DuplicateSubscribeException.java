package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.DUPLICATE_SUBSCRIBE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class DuplicateSubscribeException extends BaseException {
    public DuplicateSubscribeException() {
        super(BAD_REQUEST, DUPLICATE_SUBSCRIBE.getMessage());
    }
}
