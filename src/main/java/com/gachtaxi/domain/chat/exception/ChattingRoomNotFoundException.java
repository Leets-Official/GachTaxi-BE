package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.CHATTING_ROOM_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ChattingRoomNotFoundException extends BaseException {
    public ChattingRoomNotFoundException() {
        super(NOT_FOUND, CHATTING_ROOM_NOT_FOUND.getMessage());
    }
}
