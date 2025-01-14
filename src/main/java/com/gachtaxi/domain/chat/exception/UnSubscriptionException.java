package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.UN_SUBSCRIBE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class UnSubscriptionException extends BaseException {
    public UnSubscriptionException() {
        super(BAD_REQUEST, UN_SUBSCRIBE.getMessage());
    }
}
