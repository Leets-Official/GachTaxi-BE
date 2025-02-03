package com.gachtaxi.domain.members.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    // Member
    DUPLICATED_NICKNAME("중복되는 닉네임입니다."),
    DUPLICATED_STUDENT_NUMBER("이미 가입된 학번입니다."),
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    DUPLICATED_EMAIL("이미 가입된 이메일이에요!"),
    EMAIL_FROM_INVALID("가천대학교 이메일이 아니에요!"),

    // Blacklist
    BLACKLIST_ALREADY_EXISTS("이미 등록된 블랙리스트입니다."),
    BLACKLIST_NOT_FOUND("블랙리스트를 찾을 수 없습니다."),
    BLACKLIST_REQUESTER_EQUALS_RECEIVER("본인을 블랙리스트에 추가할 수 없습니다."),
    BLACKLISTED_USER_CANNOT_JOIN("블랙리스트로 등록된 사용자의 방은 입장할 수 없습니다.");

    private final String message;
}
