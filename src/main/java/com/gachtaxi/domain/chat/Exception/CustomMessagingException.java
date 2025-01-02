package com.gachtaxi.domain.chat.Exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.Exception.ErrorMessage.MESSAGING_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class CustomMessagingException extends BaseException {
    public CustomMessagingException(String message) {
        super(MESSAGING_ERROR.getMessage() + message, INTERNAL_SERVER_ERROR);
    }
}
