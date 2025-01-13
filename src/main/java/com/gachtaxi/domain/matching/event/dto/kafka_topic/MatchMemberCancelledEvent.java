package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MatchMemberCancelledEvent(
  Long roomId,
  Long memberId,

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime canceledAt
) {

}
