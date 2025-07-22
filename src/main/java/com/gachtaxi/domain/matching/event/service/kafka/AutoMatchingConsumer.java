package com.gachtaxi.domain.matching.event.service.kafka;

import com.gachtaxi.domain.matching.common.service.MatchingRoomService;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCompletedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoMatchingConsumer {

  private final MatchingRoomService matchingRoomService;

  /**
   * 방 생성 이벤트 구독
   */
  @KafkaListener(
      topics = "${gachtaxi.kafka.topics.match-room-created}",
      containerFactory = "matchRoomCreatedEventListenerFactory"
  )
    public void onMatchRoomCreated(MatchRoomCreatedEvent event, Acknowledgment ack) {
      try {
        log.info("[KAFKA CONSUMER] Received MatchRoomCreatedEvent: {}", event);

        this.matchingRoomService.createMatchingRoom(event);

        ack.acknowledge();
    } catch (Exception e) {
      log.error("[KAFKA CONSUMER] Error processing MatchRoomCreatedEvent", e);
    }
  }

  /**
   * 방 멤버 참가 이벤트 구독
   */
  @KafkaListener(
      topics = "${gachtaxi.kafka.topics.match-member-joined}",
      containerFactory = "matchMemberJoinedEventListenerFactory"
  )
  public void onMatchMemberJoined(MatchMemberJoinedEvent event, Acknowledgment ack) {
    try {
      log.info("[KAFKA CONSUMER] Received MatchMemberJoinedEvent: {}", event);

      this.matchingRoomService.joinMemberToMatchingRoom(event);

      ack.acknowledge();
    } catch (Exception e) {
      log.error("[KAFKA CONSUMER] Error processing MatchMemberJoinedEvent", e);
    }
  }

  /**
   * 방 멤버 취소 이벤트 구독
   */
  @KafkaListener(
      topics = "${gachtaxi.kafka.topics.match-member-cancelled}",
      containerFactory = "matchMemberCancelledEventListenerFactory"
  )
  public void onMatchMemberLeft(MatchMemberCancelledEvent event, Acknowledgment ack) {
    try {
      log.info("[KAFKA CONSUMER] Received MatchMemberLeftEvent: {}", event);

      this.matchingRoomService.leaveMemberFromMatchingRoom(event);

      ack.acknowledge();
    } catch (Exception e) {
      log.error("[KAFKA CONSUMER] Error processing MatchMemberLeftEvent", e);
    }
  }

  /**
   * 방 취소 이벤트 구독
   */
  @KafkaListener(
      topics = "${gachtaxi.kafka.topics.match-room-cancelled}",
      containerFactory = "matchRoomCancelledEventListenerFactory"
  )
  public void onMatchRoomCancelled(MatchRoomCancelledEvent event, Acknowledgment ack) {
    try {
      log.info("[KAFKA CONSUMER] Received MatchRoomCancelledEvent: {}", event);

      this.matchingRoomService.cancelMatchingRoom(event);

      ack.acknowledge();
    } catch (Exception e) {
      log.error("[KAFKA CONSUMER] Error processing MatchRoomCancelledEvent", e);
    }
  }

  /**
   * 방 완료 이벤트 구독
   */
  @KafkaListener(
      topics = "${gachtaxi.kafka.topics.match-room-completed}",
      containerFactory = "matchRoomCompletedEventListenerFactory"
  )
  public void onMatchingRoomCompleted(MatchRoomCompletedEvent event, Acknowledgment ack) {
    try {
      log.info("[KAFKA CONSUMER] Received MatchingRoomCompletedEvent: {}", event);

      this.matchingRoomService.completeMatchingRoom(event);

      ack.acknowledge();
    } catch (Exception e) {
      log.error("[KAFKA CONSUMER] Error processing MatchingRoomCompletedEvent", e);
    }
  }
}