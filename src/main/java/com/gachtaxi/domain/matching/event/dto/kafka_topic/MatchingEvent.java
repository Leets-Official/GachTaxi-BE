package com.gachtaxi.domain.matching.event.dto.kafka_topic;

public interface MatchingEvent {

  Object getKey();
  String getTopic();
}
