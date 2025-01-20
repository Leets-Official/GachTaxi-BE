package com.gachtaxi.domain.chat.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.chat.exception.ErrorMessage.CHATTING_PARTICIPANT_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ChattingParticipantNotFoundException extends BaseException {
    public ChattingParticipantNotFoundException() {
        super(NOT_FOUND, CHATTING_PARTICIPANT_NOT_FOUND.getMessage());
    }
}
