package com.gachtaxi.domain.notification.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    NOTIFICATION_GET_SUCCESS("알림 조회에 성공했습니다."),
    NOTIFICATION_DELETE_SUCCESS("알림 삭제에 성공했습니다.");

    private final String message;
}
