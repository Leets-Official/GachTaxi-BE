package com.gachtaxi.domain.members.dto.request;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.entity.enums.Role;
import lombok.Builder;

@Builder
public record TmpMemberDto(
   Long userId,
   String email,
   Role role
) {
    public static TmpMemberDto of(Members tmpMember) {
        return TmpMemberDto.builder()
                .userId(tmpMember.getId())
                .email(tmpMember.getEmail())
                .role(tmpMember.getRole())
                .build();
    }
}
