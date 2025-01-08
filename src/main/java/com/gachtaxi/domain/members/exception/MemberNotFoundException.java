package com.gachtaxi.domain.members.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.members.exception.ErrorMessage.MEMBER_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class MemberNotFoundException extends BaseException {
    public MemberNotFoundException() {
        super(BAD_REQUEST, MEMBER_NOT_FOUND.getMessage());
    }
}
