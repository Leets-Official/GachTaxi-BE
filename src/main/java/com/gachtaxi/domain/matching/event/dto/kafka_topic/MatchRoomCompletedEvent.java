package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MatchRoomCompletedEvent(
    Long roomId
) {

  public static MatchRoomCompletedEvent of(Long roomId) {
    return MatchRoomCompletedEvent.builder().roomId(roomId).build();
  }
}
