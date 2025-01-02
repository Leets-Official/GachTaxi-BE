package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class RedisSubscribeException extends BaseException {
    public RedisSubscribeException(String message) {
        super(INTERNAL_SERVER_ERROR, REDIS_SUB_ERROR.getMessage() + message);
    }
}
