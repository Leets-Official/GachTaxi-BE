package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.CONTROLLER_NOT_HAS_CURRENT_MEMBER_ID;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.gachtaxi.global.common.exception.BaseException;

public class ControllerNotHasCurrentMemberIdException extends BaseException {

  public ControllerNotHasCurrentMemberIdException() {
    super(INTERNAL_SERVER_ERROR, CONTROLLER_NOT_HAS_CURRENT_MEMBER_ID.getMessage());
  }
}
