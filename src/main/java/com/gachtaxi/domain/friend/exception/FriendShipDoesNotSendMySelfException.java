package com.gachtaxi.domain.friend.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.friend.exception.ErrorMessage.FRIEND_DO_NOT_SEND_MYSELF;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class FriendShipDoesNotSendMySelfException extends BaseException {
    public FriendShipDoesNotSendMySelfException() {
        super(BAD_REQUEST, FRIEND_DO_NOT_SEND_MYSELF.getMessage());
    }
}
