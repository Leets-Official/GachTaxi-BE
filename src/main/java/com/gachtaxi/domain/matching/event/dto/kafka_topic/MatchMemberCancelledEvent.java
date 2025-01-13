package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MatchMemberCancelledEvent(
  Long roomId,
  Long memberId,

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime canceledAt
) {

  public static MatchMemberCancelledEvent of(Long roomId, Long memberId) {
    return MatchMemberCancelledEvent.builder().
        roomId(roomId)
        .memberId(memberId)
        .build();
  }
}
