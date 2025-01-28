package com.gachtaxi.domain.friend.dto.response;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.entity.enums.Gender;
import lombok.Builder;

@Builder
public record FriendsResponseDto(
        Long friendsId,
        String friendsNickName,
        String friendsProfileUrl,
        Gender gender
) {
    public static FriendsResponseDto from(Members friends) {
        return FriendsResponseDto.builder()
                .friendsId(friends.getId())
                .friendsNickName(friends.getNickname())
                .friendsProfileUrl(friends.getProfilePicture())
                .gender(friends.getGender())
                .build();
    }
}
