package com.gachtaxi.domain.friend.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.friend.exception.ErrorMessage.FRIEND_SHIP_EXISTS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class FriendShipExistsException extends BaseException {
    public FriendShipExistsException() {
        super(BAD_REQUEST, FRIEND_SHIP_EXISTS.getMessage());
    }
}
