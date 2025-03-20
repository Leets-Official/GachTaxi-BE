package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.NOT_MATCH_GENDER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class NotMatchingGenderException extends BaseException {
    public NotMatchingGenderException() {
        super(BAD_REQUEST, NOT_MATCH_GENDER.getMessage());
    }
}
