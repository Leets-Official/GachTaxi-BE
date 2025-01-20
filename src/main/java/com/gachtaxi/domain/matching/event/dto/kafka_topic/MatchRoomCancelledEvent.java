package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

@Builder(access = AccessLevel.PRIVATE)
public record MatchRoomCancelledEvent(
  Long roomId,

  String topic
) implements MatchingEvent{

  @Override
  public Object getKey() {
    return String.valueOf(this.roomId);
  }

  @Override
  public String getTopic() {
    return this.topic;
  }

  public static MatchRoomCancelledEvent of(Long roomId, String topic) {
    return MatchRoomCancelledEvent.builder()
        .roomId(roomId)
        .topic(topic)
        .build();
  }
}
