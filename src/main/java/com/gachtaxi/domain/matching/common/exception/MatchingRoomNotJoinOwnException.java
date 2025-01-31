package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.MATCHING_ROOM_NOT_JOIN_OWN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class MatchingRoomNotJoinOwnException extends BaseException {
    public MatchingRoomNotJoinOwnException() {
        super(BAD_REQUEST, MATCHING_ROOM_NOT_JOIN_OWN.getMessage());
    }
}
