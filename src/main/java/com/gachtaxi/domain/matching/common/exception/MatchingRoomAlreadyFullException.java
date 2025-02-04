package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.MATCHING_ALREADY_ROOM_FULL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class MatchingRoomAlreadyFullException extends BaseException {

      public MatchingRoomAlreadyFullException() {
          super(BAD_REQUEST, MATCHING_ALREADY_ROOM_FULL.getMessage());
      }
}
