package com.gachtaxi.domain.members.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.gachtaxi.domain.members.exception.ErrorMessage.DUPLICATED_NICKNAME;

public class DuplicatedNickNameException extends BaseException {
    public DuplicatedNickNameException() {
        super(HttpStatus.BAD_REQUEST, DUPLICATED_NICKNAME.getMessage());
    }
}
