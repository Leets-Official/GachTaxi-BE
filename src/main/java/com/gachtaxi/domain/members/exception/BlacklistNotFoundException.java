package com.gachtaxi.domain.members.exception;

import static com.gachtaxi.domain.members.exception.ErrorMessage.BLACKLIST_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.gachtaxi.global.common.exception.BaseException;

public class BlacklistNotFoundException extends BaseException {

    public BlacklistNotFoundException() {
        super(NOT_FOUND, BLACKLIST_NOT_FOUND.getMessage());
    }
}
