package com.gachtaxi.domain.members.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NoSuchMemberException extends BaseException {

  public NoSuchMemberException() {
    super(HttpStatus.NOT_FOUND, ErrorMessage.NO_SUCH_MEMBER.getMessage());
  }
}
