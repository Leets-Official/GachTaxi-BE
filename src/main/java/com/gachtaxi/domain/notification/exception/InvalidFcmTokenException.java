package com.gachtaxi.domain.notification.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.notification.exception.ErrorMessage.INVALID_FCM_TOKEN;
import static org.springframework.http.HttpStatus.valueOf;

public class InvalidFcmTokenException extends BaseException {
    public InvalidFcmTokenException(int statusCode, String statusMessage) {
        super(valueOf(statusCode), statusMessage + INVALID_FCM_TOKEN.getMessage());
    }
}
