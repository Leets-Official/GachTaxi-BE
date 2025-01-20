package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.NO_SUCH_MATCHING_ROOM;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.gachtaxi.global.common.exception.BaseException;

public class NoSuchMatchingRoomException extends BaseException {

  public NoSuchMatchingRoomException() {
    super(NOT_FOUND, NO_SUCH_MATCHING_ROOM.getMessage());
  }
}
