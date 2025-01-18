package com.gachtaxi.domain.members.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.members.exception.ErrorMessage.DUPLICATED_NICKNAME;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class DuplicatedNickNameException extends BaseException {
    public DuplicatedNickNameException() {
        super(BAD_REQUEST, DUPLICATED_NICKNAME.getMessage());
    }
}
