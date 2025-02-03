package com.gachtaxi.domain.members.exception;

import static com.gachtaxi.domain.members.exception.ErrorMessage.BLACKLISTED_USER_CANNOT_JOIN;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.gachtaxi.global.common.exception.BaseException;

public class BlacklistedUserCannotJoinException extends BaseException {
    public BlacklistedUserCannotJoinException() {
        super(FORBIDDEN, BLACKLISTED_USER_CANNOT_JOIN.getMessage());
    }
}
