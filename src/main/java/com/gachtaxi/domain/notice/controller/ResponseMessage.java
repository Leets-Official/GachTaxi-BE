package com.gachtaxi.domain.notice.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    GET_NOTICE_SUCCESS("채팅방 상세 조회에 성공했습니다."),
    GET_NOTICE_ALL_SUCCESS("공지사항 전체조회에 성공했습니다.");

    private final String message;

}
