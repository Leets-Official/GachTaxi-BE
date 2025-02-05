package com.gachtaxi.domain.matching.common.dto.response;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record MatchingRoomListResponse(
        List<MatchingRoomResponse> rooms,
        MatchingPageableResponse pageable
) {
    public static MatchingRoomListResponse of(Slice<MatchingRoomResponse> slice) {
        return MatchingRoomListResponse.builder()
                .rooms(slice.getContent().stream().toList())
                .pageable(MatchingPageableResponse.of(slice))
                .build();
    }
}
