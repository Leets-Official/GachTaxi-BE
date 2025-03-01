package com.gachtaxi.domain.matching.common.dto.response;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record MyRoomsListResponse(
        List<MyRoomResponse> rooms,
        MatchingPageableResponse pageable
) {
    public static MyRoomsListResponse of(Slice<MyRoomResponse> slice) {
        return MyRoomsListResponse.builder()
                .rooms(slice.getContent())
                .pageable(MatchingPageableResponse.of(slice))
                .build();
    }
}
