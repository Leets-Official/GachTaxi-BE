package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.ALREADY_IN_MATCHING_ROOM;
import static org.springframework.http.HttpStatus.CONFLICT;

import com.gachtaxi.global.common.exception.BaseException;
import java.util.Map;

public class AlreadyInMatchingRoomException extends BaseException {
    public AlreadyInMatchingRoomException(Long chattingRoomId) {
        super(CONFLICT, Map.of("chattingRoomId", chattingRoomId, "message", ALREADY_IN_MATCHING_ROOM.getMessage()).toString());
    }

    public AlreadyInMatchingRoomException() {
        super(CONFLICT, ALREADY_IN_MATCHING_ROOM.getMessage());
    }

}
