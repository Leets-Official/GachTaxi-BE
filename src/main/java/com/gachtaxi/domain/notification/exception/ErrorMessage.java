package com.gachtaxi.domain.notification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    SSE_NOT_SUBSCRIBED("SSE를 구독하고 있지 않습니다."),
    NOTIFICATION_NOT_FOUND("존재하지 않는 알림입니다."),
    FCM_TOKEN_NOT_FOUND(" Fcm 토큰이 존재하지 않습니다"),
    INVALID_FCM_TOKEN("Fcm 토큰 형식이 올바르지 않습니다."),
    INVALID_MEMBER_MATCH("알림을 삭제할 권한이 없습니다.");

    private final String message;
}
