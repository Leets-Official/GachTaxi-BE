package com.gachtaxi.domain.friend.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.friend.exception.ErrorMessage.FRIEND_PENDING;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class FriendShipPendingException extends BaseException {
    public FriendShipPendingException() {
        super(BAD_REQUEST, FRIEND_PENDING.getMessage());
    }
}
