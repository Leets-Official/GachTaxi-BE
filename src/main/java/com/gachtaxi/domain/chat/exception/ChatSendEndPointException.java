package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.CHAT_SEND_END_POINT_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ChatSendEndPointException extends BaseException {
    public ChatSendEndPointException() {
        super(BAD_REQUEST, CHAT_SEND_END_POINT_ERROR.getMessage());
    }
}
