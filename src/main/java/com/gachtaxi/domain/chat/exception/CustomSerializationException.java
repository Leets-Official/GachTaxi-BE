package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.SERIALIZATION_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class CustomSerializationException extends BaseException {
    public CustomSerializationException(String message) {
        super(INTERNAL_SERVER_ERROR, SERIALIZATION_ERROR.getMessage() + message);
    }
}
