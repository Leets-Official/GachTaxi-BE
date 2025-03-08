package com.gachtaxi.domain.members.exception;

import static com.gachtaxi.domain.members.exception.ErrorMessage.INVALID_NICKNAME_LENGTH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class InvalidNicknameLengthException extends BaseException {
    public InvalidNicknameLengthException() {
        super(BAD_REQUEST, INVALID_NICKNAME_LENGTH.getMessage());
    }
}
