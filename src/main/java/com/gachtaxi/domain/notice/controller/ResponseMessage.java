package com.gachtaxi.domain.notice.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    NOTICE_GET_SUCCESS("공지사항 상세 조회에 성공했습니다."),
    NOTICE_GET_ALL_SUCCESS("공지사항 전체조회에 성공했습니다."),

    // 어드민 관련
    NOTICE_CREATE_SUCCESS("공지사항 생성에 성공했습니다."),
    NOTICE_UPDATE_SUCCESS("공지사항 수정에 성공했습니다."),
    NOTICE_DELETE_SUCCESS("공지사항 삭제에 성공했습니다.");

    private final String message;

}
