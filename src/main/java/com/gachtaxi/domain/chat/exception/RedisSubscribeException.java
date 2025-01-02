package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class RedisSubscribeException extends BaseException {
    public RedisSubscribeException() {
        super(REDIS_SUB_ERROR.getMessage(), INTERNAL_SERVER_ERROR);
    }
}
