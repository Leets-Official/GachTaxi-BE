package com.gachtaxi.domain.chat.dto.request;

public record ChatMessage(
        String message,
        Long roomId
) {
}
