package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.domain.members.entity.enums.Gender;
import lombok.Builder;

import java.util.List;

@Builder
public record MemberWithFriendStatusDetailResponse(
        Long userId,
        String nickName,
        String profilePicture,
        Gender gender,
        boolean isSendFriendsRequest

) {
    public static MemberWithFriendStatusDetailResponse of(MemberWithFriendRequestProjection projection) {

        return MemberWithFriendStatusDetailResponse.builder()
                .userId(projection.member().getId())
                .nickName(projection.member().getNickname())
                .profilePicture(projection.member().getProfilePicture())
                .gender(projection.member().getGender())
                .isSendFriendsRequest(projection.isSendFriendsRequest())
                .build();

    }
}
