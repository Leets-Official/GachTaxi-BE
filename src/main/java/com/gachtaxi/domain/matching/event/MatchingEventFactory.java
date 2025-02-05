package com.gachtaxi.domain.matching.event;

import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingPostRequest;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCompletedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MatchingEventFactory {

  @Value("${gachtaxi.kafka.topics.match-member-cancelled}")
  private String matchMemberCancelledTopic;

  @Value("${gachtaxi.kafka.topics.match-member-joined}")
  private String matchMemberJoinedTopic;

  @Value("${gachtaxi.kafka.topics.match-room-cancelled}")
  private String matchRoomCancelledTopic;

  @Value("${gachtaxi.kafka.topics.match-room-completed}")
  private String matchRoomCompletedTopic;

  @Value("${gachtaxi.kafka.topics.match-room-created}")
  private String matchRoomCreatedTopic;

  @Value("${gachtaxi.matching.auto-matching-max-capacity}")
  private int autoMaxCapacity;

  @Value("${gachtaxi.matching.auto-matching-description}")
  private String autoDescription;

  public MatchMemberCancelledEvent createMatchMemberCancelledEvent(Long roomId, Long memberId) {
    return MatchMemberCancelledEvent.of(roomId, memberId, this.matchMemberCancelledTopic);
  }

  public MatchMemberJoinedEvent createMatchMemberJoinedEvent(Long roomId, Long memberId, Long chattingRoomId) {
    return MatchMemberJoinedEvent.of(roomId, memberId, this.matchMemberJoinedTopic, chattingRoomId);
  }

  public MatchRoomCancelledEvent createMatchRoomCancelledEvent(Long roomId) {
    return MatchRoomCancelledEvent.of(roomId, this.matchRoomCancelledTopic);
  }

  public MatchRoomCompletedEvent createMatchRoomCompletedEvent(Long roomId) {
    return MatchRoomCompletedEvent.of(roomId, this.matchRoomCompletedTopic);
  }

  public MatchRoomCreatedEvent createMatchRoomCreatedEvent(Long memberId, AutoMatchingPostRequest autoMatchingPostRequest) {
    return MatchRoomCreatedEvent.of(memberId, autoMatchingPostRequest, this.autoMaxCapacity, this.autoDescription, this.matchRoomCreatedTopic);
  }
}
