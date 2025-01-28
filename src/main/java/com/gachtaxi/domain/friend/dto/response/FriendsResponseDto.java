package com.gachtaxi.domain.friend.dto.response;

import com.gachtaxi.domain.members.entity.Members;
import lombok.Builder;

@Builder
public record FriendsResponseDto(
        Long friendsId,
        String friendsNickName,
        String friendsProfileUrl
) {
    public static FriendsResponseDto from(Members friends) {
        return FriendsResponseDto.builder()
                .friendsId(friends.getId())
                .friendsNickName(friends.getNickname())
                .friendsProfileUrl(friends.getProfilePicture())
                .build();
    }
}
