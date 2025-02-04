package com.gachtaxi.domain.members.exception;

import static com.gachtaxi.domain.members.exception.ErrorMessage.BLACKLIST_REQUESTER_EQUALS_RECEIVER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.gachtaxi.global.common.exception.BaseException;

public class BlacklistRequesterEqualsReceiverException extends BaseException {

  public BlacklistRequesterEqualsReceiverException() {
    super(BAD_REQUEST, BLACKLIST_REQUESTER_EQUALS_RECEIVER.getMessage());
  }

}
