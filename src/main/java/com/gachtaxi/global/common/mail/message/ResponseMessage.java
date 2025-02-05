package com.gachtaxi.global.common.mail.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    EMAIL_SEND_SUCCESS("인증 번호를 발송했습니다! 이메일을 확인하세요!"),
    EMAIL_AUTHENTICATION_SUCESS("이메일 인증에 성공했습니다!"),
    AGREEEMENT_UPDATE_SUCCESS("약관 동의 정보를 업데이트 했습니다!"),
    SUPPLEMENT_UPDATE_SUCCESS("사용자 추가 정보를 업데이트 했습니다!"),
    EMAIL_TEMPLATE_CREATE_SUCCESS("이메일 템플릿을 생성했습니다.");

    private final String message;
}
