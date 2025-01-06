package com.gachtaxi.domain.matching.common.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NoSuchMatchingRoomException extends BaseException {

  public NoSuchMatchingRoomException() {
    super(HttpStatus.NOT_FOUND, ErrorMessage.NO_SUCH_MATCHING_ROOM.getMessage());
  }
}
