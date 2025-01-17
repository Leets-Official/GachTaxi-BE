package com.gachtaxi.domain.notification.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.gachtaxi.domain.notification.exception.ErrorMessage.*;

public class FcmTokenNotFoundException extends BaseException {
    public FcmTokenNotFoundException(int statusCode, String statusMessage) {
        super(HttpStatus.valueOf(statusCode), statusMessage + FCM_TOKEN_NOT_FOUND.getMessage());
    }
}
