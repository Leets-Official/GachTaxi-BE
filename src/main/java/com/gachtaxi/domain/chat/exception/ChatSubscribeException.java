package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.CHAT_SUBSCRIBE_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ChatSubscribeException extends BaseException {
    public ChatSubscribeException() {
        super(BAD_REQUEST, CHAT_SUBSCRIBE_ERROR.getMessage());
    }
}
