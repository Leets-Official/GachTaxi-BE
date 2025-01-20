package com.gachtaxi.domain.chat.dto.response;

import org.springframework.data.util.Pair;

public record ReadMessageRange(
        String startMessageId,
        String endMessageId
) {
    public static ReadMessageRange of(Pair<String, String> pair) {
        return new ReadMessageRange(pair.getFirst(), pair.getSecond());
    }
}
