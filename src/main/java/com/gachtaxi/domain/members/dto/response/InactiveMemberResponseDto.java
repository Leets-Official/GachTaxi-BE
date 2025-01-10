package com.gachtaxi.domain.members.dto.response;

import lombok.Builder;

@Builder
public record InactiveMemberResponseDto(
        Long userId
) {
    public static InactiveMemberResponseDto from(Long userId) {
        return InactiveMemberResponseDto.builder()
                .userId(userId)
                .build();
    }

}
