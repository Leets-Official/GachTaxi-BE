package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.domain.members.entity.Members;
import lombok.Builder;

@Builder
public record AccountGetResponse(
    Long userId,
    Long studentNumber,
    String nickName,
    String realName,
    String profilePicture,
    String email,
    String role,
    String gender,
    String accountNumber
) {

  public static AccountGetResponse of(Members members) {
    return AccountGetResponse.builder()
        .userId(members.getId())
        .studentNumber(members.getStudentNumber())
        .nickName(members.getNickname())
        .realName(members.getRealName())
        .profilePicture(members.getProfilePicture())
        .email(members.getEmail())
        .role(members.getRole().name())
        .gender(members.getGender().name())
        .accountNumber(members.getAccountNumber())
        .build();
  }
}
