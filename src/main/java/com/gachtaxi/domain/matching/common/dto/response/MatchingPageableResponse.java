package com.gachtaxi.domain.matching.common.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;


@Builder
public record MatchingPageableResponse(
        int pageNumber,
        int pageSize,
        int numberOfElements,
        boolean last
) {
    public static MatchingPageableResponse of(Page<?> page) {
        return MatchingPageableResponse.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .last(page.isLast())
                .build();
    }
}
