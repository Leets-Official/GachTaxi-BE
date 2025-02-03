package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.NO_SUCH_INVITATION;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.gachtaxi.global.common.exception.BaseException;

public class NoSuchInvitationException extends BaseException {
    public NoSuchInvitationException() {
        super(NOT_FOUND, NO_SUCH_INVITATION.getMessage());
    }
}
