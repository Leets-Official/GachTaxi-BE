package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.LAST_TIME_STAMP_NULL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class LastMessageTimeStampNullException extends BaseException {
    public LastMessageTimeStampNullException() {
        super(BAD_REQUEST, LAST_TIME_STAMP_NULL.getMessage());
    }
}
