package com.gachtaxi.domain.notice.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record NoticePageableResponse(
        int pageNumber,
        int pageSize,
        int numberOfElements,
        boolean isLast
) {
    public static NoticePageableResponse of(Slice<?> slice) {
        return NoticePageableResponse.builder()
                .pageNumber(slice.getNumber())
                .pageSize(slice.getSize())
                .numberOfElements(slice.getNumberOfElements())
                .isLast(slice.isLast())
                .build();
    }
}
