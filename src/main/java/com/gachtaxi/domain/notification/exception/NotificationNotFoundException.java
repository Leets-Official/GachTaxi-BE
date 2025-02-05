package com.gachtaxi.domain.notification.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.notification.exception.ErrorMessage.NOTIFICATION_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotificationNotFoundException extends BaseException {
    public NotificationNotFoundException() {
        super(NOT_FOUND, NOTIFICATION_NOT_FOUND.getMessage());
    }
}
