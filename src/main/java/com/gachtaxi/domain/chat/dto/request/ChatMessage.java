package com.gachtaxi.domain.chat.dto.request;

import java.time.LocalDateTime;

public record ChatMessage(
        long roomId,
        long senderId,
        String message,
        LocalDateTime timeStamp
) {
    public static ChatMessage of(long roomId, long senderId, String message, LocalDateTime timeStamp) {
        return new ChatMessage(roomId, senderId, message, timeStamp);
    }
}
