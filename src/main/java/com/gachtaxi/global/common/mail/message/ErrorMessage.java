package com.gachtaxi.global.common.mail.message;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    AUTH_CODE_NOT_MATCH("인증 코드가 일치하지 않습니다");

    private final String message;
}
