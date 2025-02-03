package com.gachtaxi.domain.friend.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.friend.exception.ErrorMessage.FRIEND_NOT_EXISTS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class FriendNotExistsException extends BaseException {
    public FriendNotExistsException() {
        super(BAD_REQUEST, FRIEND_NOT_EXISTS.getMessage());
    }
}
