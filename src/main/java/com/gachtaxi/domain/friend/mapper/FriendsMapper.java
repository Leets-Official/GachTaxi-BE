package com.gachtaxi.domain.friend.mapper;

import com.gachtaxi.domain.friend.dto.response.FriendsResponseDto;
import com.gachtaxi.domain.friend.entity.Friends;

public class FriendsMapper {

    public static FriendsResponseDto toResponseDto(Friends friends, Long memberId) {
        if(friends.getSender().getId().equals(memberId)) {
            return FriendsResponseDto.from(friends.getReceiver());
        }else{
            return FriendsResponseDto.from(friends.getSender());
        }
    }
}
