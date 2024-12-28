package com.gachtaxi.global.common.exception.handler;

import com.gachtaxi.global.common.exception.BaseException;
import com.gachtaxi.global.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.gachtaxi.global.common.exception.BaseErrorMessage.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // response format
    private static final String LOG_FORMAT = "Class: {}, Code : {}, Message : {}";

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(BaseException e) {

        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getMessage());
        ApiResponse<Void> response = ApiResponse.fail(e.getErrorCode(), e.getMessage());

        return ResponseEntity
                .status(e.getErrorCode())
                .body(response);
    }
}