package com.gachtaxi.domain.members.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record MemberWithFriendStatusSlice(
        List<MemberWithFriendStatusDetailResponse> memberList,
        MemberPageableResponse pageable

) {
    public static MemberWithFriendStatusSlice of(List<MemberWithFriendStatusDetailResponse> memberList, MemberPageableResponse pageable) {
        return MemberWithFriendStatusSlice.builder()
                .memberList(memberList)
                .pageable(pageable)
                .build();
    }
}
