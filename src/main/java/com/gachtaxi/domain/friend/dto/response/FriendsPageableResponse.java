package com.gachtaxi.domain.friend.dto.response;

import com.gachtaxi.domain.friend.entity.Friends;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record FriendsPageableResponse(
        int pageNum,
        int pageSize,
        int numberOfElements,
        boolean isLast
) {
    public static FriendsPageableResponse from(Slice<Friends> slice) {
        return FriendsPageableResponse.builder()
                .pageNum(slice.getNumber())
                .pageSize(slice.getSize())
                .numberOfElements(slice.getNumberOfElements())
                .isLast(slice.isLast())
                .build();
    }
}
