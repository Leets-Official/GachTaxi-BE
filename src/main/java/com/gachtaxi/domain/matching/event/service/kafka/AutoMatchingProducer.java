package com.gachtaxi.domain.matching.event.service.kafka;

import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoMatchingProducer {

  private final KafkaTemplate<String, MatchRoomCreatedEvent> matchRoomCreatedEventKafkaTemplate;
  private final KafkaTemplate<String, MatchMemberJoinedEvent> matchMemberJoinedEventKafkaTemplate;

  @Value("${kafka.topic.match-room-created}")
  private String matchRoomCreatedTopic;

  @Value("${kafka.topic.match-member-joined}")
  private String matchMemberJoinedTopic;

  /**
   * 방 생성 이벤트를 발행
   */
  public void sendMatchRoomCreatedEvent(MatchRoomCreatedEvent matchRoomCreatedEvent) {
    String key = matchRoomCreatedEvent.title();

    CompletableFuture<?> future = this.matchRoomCreatedEventKafkaTemplate.send(matchRoomCreatedTopic, key, matchRoomCreatedEvent);

    future.thenAccept(result -> {
          if (result instanceof RecordMetadata metadata) {
            log.info("[KAFKA PRODUCER] Success sending MatchRoomCreatedEvent: "
                    + "topic={}, partition={}, offset={}, key={}",
                metadata.topic(), metadata.partition(), metadata.offset(), key
            );
          }
        }
    ).exceptionally(ex -> {
      log.error("[KAFKA PRODUCER] Failed to send MatchRoomCreatedEvent key={}", key, ex);
      return null;
    });
  }

  /**
   * 방 멤버 참가 이벤트를 발행
   */
  public void sendMatchMemberJoinedEvent(MatchMemberJoinedEvent matchMemberJoinedEvent) {
    String key = String.valueOf(matchMemberJoinedEvent.roomId());

    CompletableFuture<?> future = this.matchMemberJoinedEventKafkaTemplate.send(matchMemberJoinedTopic, key, matchMemberJoinedEvent);

    future.thenAccept(result -> {
          if (result instanceof RecordMetadata metadata) {
            log.info("[KAFKA PRODUCER] Success sending MatchMemberJoinedEvent: "
                    + "topic={}, partition={}, offset={}, key={}",
                metadata.topic(), metadata.partition(), metadata.offset(), key
            );
          }
        }
    ).exceptionally(ex -> {
      log.error("[KAFKA PRODUCER] Failed to send MatchMemberJoinedEvent key={}", key, ex);
      return null;
    });
  }
}