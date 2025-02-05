package com.gachtaxi.domain.friend.dto.response;

import com.gachtaxi.domain.friend.entity.Friends;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record FriendsPageableResponse(
        int pageNumber,
        int pageSize,
        int numberOfElements,
        boolean last
) {
    public static FriendsPageableResponse from(Slice<Friends> slice) {
        return FriendsPageableResponse.builder()
                .pageNumber(slice.getNumber())
                .pageSize(slice.getSize())
                .numberOfElements(slice.getNumberOfElements())
                .last(slice.isLast())
                .build();
    }
}
