package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.domain.members.entity.Blacklists;

public record BlacklistPostResponse(
    Long requesterId,
    Long receiverId,
    Long blacklistId
) {
  public static BlacklistPostResponse of (Blacklists blacklists) {
    return new BlacklistPostResponse(
        blacklists.getRequester().getId(),
        blacklists.getReceiver().getId(),
        blacklists.getId()
    );
  }
}
