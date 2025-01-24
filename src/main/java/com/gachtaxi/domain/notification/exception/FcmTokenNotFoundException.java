package com.gachtaxi.domain.notification.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.notification.exception.ErrorMessage.FCM_TOKEN_NOT_FOUND;
import static org.springframework.http.HttpStatus.valueOf;


public class FcmTokenNotFoundException extends BaseException {
    public FcmTokenNotFoundException(int statusCode, String statusMessage) {
        super(valueOf(statusCode), statusMessage + FCM_TOKEN_NOT_FOUND.getMessage());
    }
}
