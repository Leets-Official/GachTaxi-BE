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
  NOT_SUBSCRIBED_SSE("SSE 구독 후 자동 매칭을 요청할 수 있습니다.");

  private final String message;
}
