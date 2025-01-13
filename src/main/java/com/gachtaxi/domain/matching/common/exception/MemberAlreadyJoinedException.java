package com.gachtaxi.domain.matching.common.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class MemberAlreadyJoinedException extends BaseException {

  public MemberAlreadyJoinedException() {
    super(HttpStatus.CONFLICT, ErrorMessage.MEMBER_ALREADY_JOINED_MATCHING_ROOM.getMessage());
  }
}
