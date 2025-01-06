package com.gachtaxi.domain.matching.common.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotActiveMatchingRoomException extends BaseException {

  public NotActiveMatchingRoomException() {
    super(HttpStatus.BAD_REQUEST, ErrorMessage.NOT_ACTIVE_MATCHING_ROOM.getMessage());
  }
}
