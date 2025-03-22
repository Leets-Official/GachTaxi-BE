package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.domain.members.entity.Members;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record MemberPageableResponse(
        int pageNumber,
        int pageSize,
        int numberOfElements,
        boolean last
) {
    public static MemberPageableResponse from(Slice<Members> slice) {
        return MemberPageableResponse.builder()
                .pageNumber(slice.getNumber())
                .pageSize(slice.getSize())
                .numberOfElements(slice.getNumberOfElements())
                .last(slice.isLast())
                .build();
    }

}
