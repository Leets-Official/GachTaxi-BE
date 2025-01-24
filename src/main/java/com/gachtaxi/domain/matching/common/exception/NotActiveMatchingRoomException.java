package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.NOT_ACTIVE_MATCHING_ROOM;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class NotActiveMatchingRoomException extends BaseException {

  public NotActiveMatchingRoomException() {
    super(BAD_REQUEST, NOT_ACTIVE_MATCHING_ROOM.getMessage());
  }
}
