package com.gachtaxi.domain.matching.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

  NO_SUCH_MATCHING_ROOM("해당 매칭 방이 존재하지 않습니다."),
  NOT_ACTIVE_MATCHING_ROOM("열린 매칭 방이 아닙니다."),
  NOT_FOUND_PAGE("페이지 번호는 0 이상이어야 합니다.");

  private final String message;
}
