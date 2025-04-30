package com.gachtaxi.domain.members.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record MemberSliceResponse(
        List<MemberSummaryResponse> memberList,
        MemberPageableResponse pageable
) {
    public static MemberSliceResponse of(List<MemberSummaryResponse> memberList, MemberPageableResponse pageable) {
        return MemberSliceResponse.builder()
                .memberList(memberList)
                .pageable(pageable)
                .build();
    }
}
