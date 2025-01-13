package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class MemberNotInMatchingRoomException extends BaseException {

  public MemberNotInMatchingRoomException() {
    super(BAD_REQUEST, MEMBER_NOT_IN_MATCHING_ROOM.getMessage());
  }
}
