package com.gachtaxi.domain.chat.dto.response;

import com.gachtaxi.domain.chat.entity.ChattingMessage;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record ChatPageableResponse(
        int pageNumber,
        int pageSize,
        int numberOfElements,
        boolean first,
        boolean last,
        boolean empty
) {
    public static ChatPageableResponse of(int pageNumber, Slice<ChattingMessage> slice) {
        return ChatPageableResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(slice.getSize())
                .numberOfElements(slice.getNumberOfElements())
                .first(slice.isFirst())
                .last(slice.isLast())
                .empty(slice.isEmpty())
                .build();
    }
}
