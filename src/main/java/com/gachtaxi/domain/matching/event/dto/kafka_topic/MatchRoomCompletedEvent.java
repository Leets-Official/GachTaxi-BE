package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import lombok.Builder;

@Builder
public record MatchRoomCompletedEvent(
    Long roomId
) {

}
