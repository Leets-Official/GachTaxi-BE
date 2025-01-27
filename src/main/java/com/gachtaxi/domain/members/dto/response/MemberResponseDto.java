package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.entity.enums.Gender;
import lombok.Builder;

@Builder
public record MemberResponseDto(
        Long userId,
        Long studentNumber,
        String nickName,
        String realName,
        String profilePicture,
        String email,
        String role,
        Gender gender
) {
    public static MemberResponseDto from(Members members) {
        return MemberResponseDto.builder()
                .userId(members.getId())
                .studentNumber(members.getStudentNumber())
                .nickName(members.getNickname())
                .realName(members.getRealName())
                .profilePicture(members.getProfilePicture())
                .email(members.getEmail())
                .role(members.getRole().name())
                .gender(members.getGender())
                .build();
    }
}
