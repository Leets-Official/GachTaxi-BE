package com.gachtaxi.domain.chat.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    SERIALIZATION_ERROR("[Redis] 데이터 직렬화에 실패했습니다"),
    MESSAGING_ERROR("STOMP 메시지 전송에 실패했습니다"),
    JSON_PROCESSING_ERROR("Json 직렬화에 실패했습니다."),
    REDIS_SUB_ERROR("[Redis] 메시지 전송에 실패했습니다.");

    private final String message;
}
