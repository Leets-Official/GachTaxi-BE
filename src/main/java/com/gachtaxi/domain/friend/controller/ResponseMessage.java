package com.gachtaxi.domain.friend.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    FRIEND_REQUEST_SUCCESS("친구 요청을 보냈습니다."),
    FRIEND_STATUS_ACCEPTED("친구 요청을 수락했습니다"),
    FRIEND_STATUS_REJECTED("친구 요청을 거절했습니다"),
    FRIEND_DELETE("친구를 삭제했습니다."),
    FRIEND_LIST_SUCCESS("친구 목록을 조회합니다");

    private final String message;
}
