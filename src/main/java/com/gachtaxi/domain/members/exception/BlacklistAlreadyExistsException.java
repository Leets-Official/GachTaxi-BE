package com.gachtaxi.domain.members.exception;

import static com.gachtaxi.domain.members.exception.ErrorMessage.BLACKLIST_ALREADY_EXISTS;
import static org.springframework.http.HttpStatus.CONFLICT;

import com.gachtaxi.global.common.exception.BaseException;

public class BlacklistAlreadyExistsException extends BaseException {

    public BlacklistAlreadyExistsException() {
        super(CONFLICT, BLACKLIST_ALREADY_EXISTS.getMessage());
    }
}
