package com.gachtaxi.domain.chat.dto.request;

public record ChatMessageRequest(
        Long roomId,
        String nickname,
        String message
) {
}
