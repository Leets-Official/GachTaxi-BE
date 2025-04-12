package com.gachtaxi.domain.notice.exception;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.CHATTING_ROOM_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.gachtaxi.global.common.exception.BaseException;

public class NoticeNotFoundException extends BaseException {

    public NoticeNotFoundException() { super(NOT_FOUND, CHATTING_ROOM_NOT_FOUND.getMessage()); }

}
