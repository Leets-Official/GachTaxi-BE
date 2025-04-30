package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.entity.enums.Gender;
import lombok.Builder;

@Builder
public record MemberSummaryResponse(
        Long userId,
        String nickName,
        String profilePicture,
        Gender gender
) {
    public static MemberSummaryResponse from(Members members) {
        return MemberSummaryResponse.builder()
                .userId(members.getId())
                .nickName(members.getNickname())
                .profilePicture(members.getProfilePicture())
                .gender(members.getGender())
                .build();
    }
}
