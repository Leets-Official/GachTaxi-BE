package com.gachtaxi.domain.matching.common.dto.response;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record MatchingRoomListResponse(
        List<MatchingRoomResponse> rooms,
        MatchingPageableResponse pageable
) {
    public static MatchingRoomListResponse of(Slice<MatchingRoomResponse> Slice) {
        return MatchingRoomListResponse.builder()
                .rooms(Slice.getContent())
                .pageable(MatchingPageableResponse.of(Slice))
                .build();
    }
}
