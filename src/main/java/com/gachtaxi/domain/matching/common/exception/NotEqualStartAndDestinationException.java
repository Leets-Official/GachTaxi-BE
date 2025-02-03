package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.NOT_EQUAL_START_DESTINATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class NotEqualStartAndDestinationException extends BaseException {
    public NotEqualStartAndDestinationException() {
        super(BAD_REQUEST, NOT_EQUAL_START_DESTINATION.getMessage());
    }
}
