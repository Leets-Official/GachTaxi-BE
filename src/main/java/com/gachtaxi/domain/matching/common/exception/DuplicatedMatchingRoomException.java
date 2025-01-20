package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.DUPLICATED_MATCHING_ROOM;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class DuplicatedMatchingRoomException extends BaseException {
    public DuplicatedMatchingRoomException() {
        super(BAD_REQUEST, DUPLICATED_MATCHING_ROOM.getMessage());
    }
}
