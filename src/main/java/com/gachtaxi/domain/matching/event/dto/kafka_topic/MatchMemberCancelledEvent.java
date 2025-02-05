package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

@Builder(access = AccessLevel.PRIVATE)
public record MatchMemberCancelledEvent(
  Long roomId,
  Long memberId,

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime canceledAt,

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

  public static MatchMemberCancelledEvent of(Long roomId, Long memberId, String topic) {
    return MatchMemberCancelledEvent.builder()
        .roomId(roomId)
        .memberId(memberId)
        .topic(topic)
        .build();
  }
}
