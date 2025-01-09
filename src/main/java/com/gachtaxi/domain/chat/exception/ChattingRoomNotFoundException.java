package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.CHATTING_ROOM_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ChattingRoomNotFoundException extends BaseException {
    public ChattingRoomNotFoundException() {
        super(BAD_REQUEST, CHATTING_ROOM_NOT_FOUND.getMessage());
    }
}
