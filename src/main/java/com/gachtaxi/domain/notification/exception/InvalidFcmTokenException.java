package com.gachtaxi.domain.notification.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.gachtaxi.domain.notification.exception.ErrorMessage.INVALID_FCM_TOKEN;
import static org.springframework.http.HttpStatus.*;

public class InvalidFcmTokenException extends BaseException {
    public InvalidFcmTokenException(int statusCode, String statusMessage) {
        super(valueOf(statusCode), statusMessage + INVALID_FCM_TOKEN.getMessage());
    }
}
