package com.gachtaxi.domain.matching.event.service.kafka;

import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import com.gachtaxi.domain.matching.event.service.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerListener {

  private final SseService sseService;

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

      // TODO: 매칭 방 생성 후 save

      this.sseService.sendToClient(event.hostMemberId(), "MATCH_ROOM_CREATED", event);

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

      // TODO: 매칭 방 멤버 참가 후 DB 상태 업데이트

      this.sseService.sendToClient(event.memberId(), "MATCH_MEMBER_JOINED", event);
      this.sseService.broadcast("MATCH_MEMBER_JOINED", event);

      ack.acknowledge();
    } catch (Exception e) {
      log.error("[KAFKA CONSUMER] Error processing MatchMemberJoinedEvent", e);
    }
  }
}