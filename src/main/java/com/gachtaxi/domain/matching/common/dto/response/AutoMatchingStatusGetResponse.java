package com.gachtaxi.domain.matching.common.dto.response;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import lombok.Builder;

@Builder
public record AutoMatchingStatusGetResponse(
    Boolean isFound,
    Integer currentMembers,
    Integer maxCapacity,
    Long roomId,
    Long chattingRoomId
) {

  public static AutoMatchingStatusGetResponse none() {
    return AutoMatchingStatusGetResponse.builder().isFound(false).build();
  }

  public static AutoMatchingStatusGetResponse of(MatchingRoom matchingRoom) {
    return AutoMatchingStatusGetResponse.builder()
        .isFound(true)
        .currentMembers(matchingRoom.getCurrentMemberCount())
        .maxCapacity(matchingRoom.getCapacity())
        .roomId(matchingRoom.getId())
        .chattingRoomId(matchingRoom.getChattingRoomId())
        .build();
  }
}
