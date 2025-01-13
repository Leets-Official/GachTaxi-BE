package com.gachtaxi.domain.matching.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

  NO_SUCH_MATCHING_ROOM("해당 매칭 방이 존재하지 않습니다."),
  NOT_ACTIVE_MATCHING_ROOM("열린 매칭 방이 아닙니다."),
  MEMBER_NOT_IN_MATCHING_ROOM("해당 매칭 방에 참가한 멤버가 아닙니다."),
  MEMBER_ALREADY_JOINED_MATCHING_ROOM("해당 맴버는 이미 매칭 방에 참가한 멤버입니다"),
  MEMBER_ALREADY_LEFT_MATCHING_ROOM("해당 멤버는 이미 매칭 방에서 나간 멤버입니다."),
  CONTROLLER_NOT_HAS_CURRENT_MEMBER_ID("해당 컨트롤러는 인가된 멤버 ID가 필요합니다.");

  private final String message;
}
