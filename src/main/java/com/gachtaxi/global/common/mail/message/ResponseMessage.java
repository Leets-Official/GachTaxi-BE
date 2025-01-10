package com.gachtaxi.global.common.mail.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    EMAIL_SEND_SUCCESS("이메일을 성공적으로 전송했습니다"),
    EMAIL_AUTHENTICATION_SUCESS("이메일 인증에 성공했습니다!"),
    EMAIL_TEMPLATE_CREATE_SUCCESS("이메일 템플릿을 생성했습니다.");

    private final String message;
}
