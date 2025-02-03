package com.gachtaxi.domain.friend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    FRIEND_DO_NOT_SEND_MYSELF("자기 자신에게 친구 요청을 보낼 수 없어요"),
    FRIEND_NOT_EXISTS("잘못된 친구 관계입니다."),
    FRIEND_EXISTS("이미 친구 입니다."),
    FRIEND_PENDING("친구 요청 대기중입니다.");

    private final String message;
}
