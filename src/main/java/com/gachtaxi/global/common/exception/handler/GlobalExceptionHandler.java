package com.gachtaxi.global.common.exception.handler;

import com.gachtaxi.global.common.exception.BaseException;
import com.gachtaxi.global.common.exception.ValidErrorResponse;
import com.gachtaxi.global.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // response format
    private static final String LOG_FORMAT = "Class: {}, Code : {}, Message : {}";
    private static final int BAD_REQUEST = 400;
    private static final int SERVER_ERROR = 500;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(BaseException e) {
        return exceptionResponse(e, e.getErrorCode());
    }

    // BindException 처리
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<List<ValidErrorResponse>>> handleValidationException(MethodArgumentNotValidException e) {
        List<ValidErrorResponse> validErrorResponses = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ValidErrorResponse.builder()
                        .errorField(fieldError.getField())
                        .errorMessage(fieldError.getDefaultMessage())
                        .inputValue(fieldError.getRejectedValue())
                        .build()
                ).toList();

        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), BAD_REQUEST, validErrorResponses);
        ApiResponse<List<ValidErrorResponse>> response = ApiResponse.response(BAD_REQUEST, "Validation failed", validErrorResponses);

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return exceptionResponse(e, SERVER_ERROR);
    }

    // 실제 예외 처리 (log + 응답)
    private ResponseEntity<ApiResponse<Void>> exceptionResponse(Exception e, int errorCode) {
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorCode, e.getMessage());
        ApiResponse<Void> response = ApiResponse.response(errorCode, e.getMessage());

        return ResponseEntity
                .status(errorCode)
                .body(response);
    }
}