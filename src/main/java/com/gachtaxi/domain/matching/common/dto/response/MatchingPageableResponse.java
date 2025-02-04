package com.gachtaxi.domain.matching.common.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Slice;


@Builder
public record MatchingPageableResponse(
        int pageNumber,
        int pageSize,
        int numberOfElements,
        boolean isLast
) {
    public static MatchingPageableResponse of(Slice<?> Slice) {
        return MatchingPageableResponse.builder()
                .pageNumber(Slice.getNumber())
                .pageSize(Slice.getSize())
                .numberOfElements(Slice.getNumberOfElements())
                .isLast(Slice.isLast())
                .build();
    }
}
