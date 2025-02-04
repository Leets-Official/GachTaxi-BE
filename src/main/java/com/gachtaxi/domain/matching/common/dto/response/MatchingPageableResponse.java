package com.gachtaxi.domain.matching.common.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Slice;


@Builder
public record MatchingPageableResponse(
        int pageNumber,
        int pageSize,
        int numberOfElements,
        boolean last
) {
    public static MatchingPageableResponse of(Slice<?> slice) {
        return MatchingPageableResponse.builder()
                .pageNumber(slice.getNumber())
                .pageSize(slice.getSize())
                .numberOfElements(slice.getNumberOfElements())
                .last(slice.isLast())
                .build();
    }
}
