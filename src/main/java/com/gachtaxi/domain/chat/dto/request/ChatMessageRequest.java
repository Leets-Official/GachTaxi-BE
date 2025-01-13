package com.gachtaxi.domain.chat.dto.request;

public record ChatMessageRequest(
        String senderName,
        String message
) {
}
