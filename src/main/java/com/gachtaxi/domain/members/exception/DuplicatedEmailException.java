package com.gachtaxi.domain.members.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.members.exception.ErrorMessage.DUPLICATED_EMAIL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class DuplicatedEmailException extends BaseException {
    public DuplicatedEmailException() {
        super(BAD_REQUEST, DUPLICATED_EMAIL.getMessage());
    }
}
