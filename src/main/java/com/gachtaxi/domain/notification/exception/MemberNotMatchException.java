package com.gachtaxi.domain.notification.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.notification.exception.ErrorMessage.INVALID_MEMBER_MATCH;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class MemberNotMatchException extends BaseException {
    public MemberNotMatchException() {
        super(FORBIDDEN, INVALID_MEMBER_MATCH.getMessage());
    }
}
