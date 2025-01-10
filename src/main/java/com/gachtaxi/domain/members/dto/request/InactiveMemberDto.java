package com.gachtaxi.domain.members.dto.request;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.entity.enums.Role;
import lombok.Builder;

@Builder
public record InactiveMemberDto(
   Long userId,
   String email,
   Role role
) {
    public static InactiveMemberDto of(Members tmpMember) {
        return InactiveMemberDto.builder()
                .userId(tmpMember.getId())
                .email(tmpMember.getEmail())
                .role(tmpMember.getRole())
                .build();
    }
}
