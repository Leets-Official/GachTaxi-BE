package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.MESSAGING_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class CustomMessagingException extends BaseException {
    public CustomMessagingException(String message) {
        super(INTERNAL_SERVER_ERROR, MESSAGING_ERROR.getMessage() + message);
    }
}
