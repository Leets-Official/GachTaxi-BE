package com.gachtaxi.domain.matching.common.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {

  // sse
  SUBSCRIBE_SUCCESS("SSE 구독에 성공했습니다."),

  // auto matching
  AUTO_MATCHING_REQUEST_ACCEPTED("자동 매칭 요청 전송에 성공했습니다."),
  NOT_SUBSCRIBED_SSE("SSE 구독 후 자동 매칭을 요청할 수 있습니다."),
  AUTO_MATCHING_REQUEST_CANCELLED("자동 매칭 취소 요청 전송에 성공했습니다."),

  // manual matching
  CREATE_MANUAL_MATCHING_ROOM_SUCCESS("수동 매칭방 생성에 성공했습니다."),
  JOIN_MANUAL_MATCHING_ROOM_SUCCESS("수동 매칭방 참여에 성공했습니다."),
  LEAVE_MANUAL_MATCHING_ROOM_SUCCESS("매칭방 퇴장이 완료되었습니다."),
  CONVERT_TO_AUTO_MATCHING_SUCCESS("자동 매칭으로 전환되었습니다."),
  GET_MANUAL_MATCHING_LIST_SUCCESS("수동 매칭방 조회에 성공했습니다."),
  GET_MY_MATCHING_LIST_SUCCESS("내 매칭방 조회에 성공했습니다.");

  private final String message;
}
