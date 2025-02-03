package com.gachtaxi.domain.friend.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FriendsSliceResponse(
        List<FriendsResponseDto> response,
        FriendsPageableResponse pageable
) {
    public static FriendsSliceResponse of(List<FriendsResponseDto> response, FriendsPageableResponse pageable) {
        return FriendsSliceResponse.builder()
                .response(response)
                .pageable(pageable)
                .build();
    }
}
