package com.gachtaxi.domain.matching.common.dto.response;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record MatchingRoomListResponse(
        List<MatchingRoomResponse> rooms,
        MatchingPageableResponse pageable
) {
    public static MatchingRoomListResponse of(Page<MatchingRoomResponse> page) {
        return MatchingRoomListResponse.builder()
                .rooms(page.getContent())
                .pageable(MatchingPageableResponse.of(page))
                .build();
    }
}
