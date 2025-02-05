package com.gachtaxi.domain.chat.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    CREATE_CHATTING_ROOM_SUCCESS("채팅방 생성에 성공했습니다."),
    GET_CHATTING_MESSAGE_SUCCESS("이전 메시지 조회에 성공 했습니다."),
    EXIT_CHATTING_ROOM_SUCCESS("채팅방 퇴장에 성공했습니다."),
    GET_CHATTING_PARTICIPANT_COUNT_SUCCESS("채팅방 전체 참여자 조회에 성공했습니다.");

    private final String message;
}
