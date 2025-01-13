package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MatchRoomCancelledEvent(
  Long roomId
) {

  public static MatchRoomCancelledEvent of(Long roomId) {
    return MatchRoomCancelledEvent.builder().roomId(roomId).build();
  }
}
