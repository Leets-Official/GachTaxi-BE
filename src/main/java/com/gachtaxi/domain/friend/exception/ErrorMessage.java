package com.gachtaxi.domain.friend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    FRIEND_SHIP_EXISTS("이미 친구 입니다."),
    FRIEND_SHIP_PENDING("친구 요청 대기중입니다.");

    private final String message;
}
