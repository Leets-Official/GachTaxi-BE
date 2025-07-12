package com.gachtaxi.domain.members.dto.response;


import com.gachtaxi.domain.members.entity.Members;

// JPQL Projection 용 DTO
public record MemberWithFriendRequestProjection(
        Members member,
        boolean isSendFriendsRequest
){

}
