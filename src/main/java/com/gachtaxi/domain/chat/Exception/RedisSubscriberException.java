package com.gachtaxi.domain.chat.Exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.Exception.ErrorMessage.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class RedisSubscriberException extends BaseException {
    public RedisSubscriberException() {
        super(REDIS_SUB_ERROR.getMessage(), INTERNAL_SERVER_ERROR);
    }
}
