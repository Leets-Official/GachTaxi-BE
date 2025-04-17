package com.gachtaxi.domain.notice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    NOTICE_NOT_FOUND("존재하지 않는 공지사항입니다");

    private final String message;
}
