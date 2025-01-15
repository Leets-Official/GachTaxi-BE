package com.gachtaxi.domain.members.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    DUPLICATED_NICKNAME("중복되는 닉네임입니다."),
    DUPLICATED_STUDENT_NUMBER("이미 가입된 학번입니다."),
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    DUPLICATED_EMAIL("이미 가입된 이메일입니다."),
    EMAIL_FROM_INVALID("가천대 이메일 형식이 아닙니다.");

    private final String message;
}
