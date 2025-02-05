package com.gachtaxi.domain.members.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.members.exception.ErrorMessage.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class MemberIdNotFoundException extends BaseException {
    public MemberIdNotFoundException() {
        super(NOT_FOUND, MEMBER_ID_NOT_FOUND.getMessage());
    }

    public static void throwException() {
        throw new MemberIdNotFoundException();
    }
}
