package com.gachtaxi.domain.members.dto.request;

public record MemberInfoRequestDto(
        String profilePicture,
        String nickName,
        String accountNumber
) {
}
