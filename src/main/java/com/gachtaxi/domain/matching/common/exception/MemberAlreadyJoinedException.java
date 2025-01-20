package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.MEMBER_ALREADY_JOINED_MATCHING_ROOM;
import static org.springframework.http.HttpStatus.CONFLICT;

import com.gachtaxi.global.common.exception.BaseException;

public class MemberAlreadyJoinedException extends BaseException {

  public MemberAlreadyJoinedException() {
    super(CONFLICT, MEMBER_ALREADY_JOINED_MATCHING_ROOM.getMessage());
  }
}
