package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.NOT_ROOM_MASTER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class NotRoomMasterException extends BaseException {
    public NotRoomMasterException() {
        super(BAD_REQUEST, NOT_ROOM_MASTER.getMessage());
    }
}
