package com.gachtaxi.global.common.exception.handler;

import com.gachtaxi.global.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class CustomMessageExceptionHandler {

    private static final String LOG_FORMAT = "Class: {}, Code : {}, Message : {}";

    @MessageExceptionHandler(RuntimeException.class)
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public ApiResponse<Void> handleRuntimeException(RuntimeException e) {
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), 500, e.getMessage());
        return ApiResponse.response(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
