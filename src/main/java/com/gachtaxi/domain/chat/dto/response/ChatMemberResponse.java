package com.gachtaxi.domain.chat.dto.response;

import com.gachtaxi.domain.members.entity.Members;
import lombok.Builder;

@Builder
public record ChatMemberResponse(
    long memberId,
    String nickName,
    String profilePicture
) {
    public static ChatMemberResponse from(Members member) {
        return ChatMemberResponse.builder()
                .memberId(member.getId())
                .nickName(member.getNickname())
                .profilePicture(member.getProfilePicture())
                .build();
    }
}
