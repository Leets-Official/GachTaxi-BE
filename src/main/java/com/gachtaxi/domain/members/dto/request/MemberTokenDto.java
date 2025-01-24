package com.gachtaxi.domain.members.dto.request;

import com.gachtaxi.domain.members.entity.Members;
import lombok.Builder;

@Builder
public record MemberTokenDto(
        Long id,
        String email,
        String role
){
    public static MemberTokenDto from(Members members){
        return MemberTokenDto.builder()
                .id(members.getId())
                .email(members.getEmail())
                .role(members.getRole().name())
                .build();
    }

    public static MemberTokenDto of(Long id, String email, String role){
        return MemberTokenDto.builder()
                .id(id)
                .email(email)
                .role(role)
                .build();
    }
}
