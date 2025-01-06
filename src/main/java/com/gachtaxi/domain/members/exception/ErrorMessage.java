package com.gachtaxi.domain.members.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    DUPLICATED_STUDENT_NUMBER("이미 가입된 학번입니다."),

    NO_SUCH_MEMBER("해당 멤버가 존재하지 않습니다.");

    private final String message;
}
