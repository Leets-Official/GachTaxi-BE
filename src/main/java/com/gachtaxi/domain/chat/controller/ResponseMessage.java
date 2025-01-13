package com.gachtaxi.domain.chat.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    CREATE_CHATTING_ROOM_SUCCESS("채팅방 생성에 성공했습니다.");

    private final String message;
}
