package com.gachtaxi.domain.matching.event.dto.kafka_topic;

public record MatchRoomCompletedEvent(
    Long roomId
) {

}
