package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MatchRoomCreatedEvent(
    Long roomId,
    Long hostMemberId,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

  public static MatchRoomCreatedEvent of(Long roomId, Long memberId) {
    return new MatchRoomCreatedEvent(roomId, memberId, LocalDateTime.now());
  }
}
