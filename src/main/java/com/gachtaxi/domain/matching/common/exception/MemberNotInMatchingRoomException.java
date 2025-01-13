package com.gachtaxi.domain.matching.common.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class MemberNotInMatchingRoomException extends BaseException {

  public MemberNotInMatchingRoomException() {
    super(HttpStatus.BAD_REQUEST, ErrorMessage.MEMBER_NOT_IN_MATCHING_ROOM.getMessage());
  }
}
