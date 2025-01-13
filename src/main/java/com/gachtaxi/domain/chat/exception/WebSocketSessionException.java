package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class WebSocketSessionException extends BaseException {
    public WebSocketSessionException(String keyword) {
        super(INTERNAL_SERVER_ERROR, keyword + WEB_SOCKET_SESSION_ATTR_NOT_FOUND.getMessage());
    }
}
