package com.gachtaxi.domain.chat.Exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.Exception.ErrorMessage.SERIALIZATION_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class CustomSerializationException extends BaseException {
    public CustomSerializationException(String message) {
        super(SERIALIZATION_ERROR.getMessage() + message, INTERNAL_SERVER_ERROR);
    }
}
