package com.gachtaxi.domain.matching.event.service.kafka;

import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCompletedEvent;
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
  private final KafkaTemplate<String, MatchMemberCancelledEvent> matchMemberCanceledEventKafkaTemplate;
  private final KafkaTemplate<String, MatchRoomCancelledEvent> matchRoomCancelledEventKafkaTemplate;
  private final KafkaTemplate<String, MatchRoomCompletedEvent> matchRoomCompletedEventKafkaTemplate;

  @Value("${gachtaxi.kafka.topics.match-room-created}")
  private String matchRoomCreatedTopic;

  @Value("${gachtaxi.kafka.topics.match-member-joined}")
  private String matchMemberJoinedTopic;

  @Value("${gachtaxi.kafka.topics.match-member-cancelled}")
  private String matchMemberCanceledTopic;

  @Value("${gachtaxi.kafka.topics.match-room-cancelled}")
  private String matchRoomCancelledTopic;

  @Value("${gachtaxi.kafka.topics.match-room-completed}")
  private String matchRoomCompletedTopic;

  /**
   * 방 생성 이벤트를 발행
   */
  public void sendMatchRoomCreatedEvent(MatchRoomCreatedEvent matchRoomCreatedEvent) {
    String key = matchRoomCreatedEvent.title();

    CompletableFuture<?> future = this.matchRoomCreatedEventKafkaTemplate.send(
        matchRoomCreatedTopic, key, matchRoomCreatedEvent);

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

    CompletableFuture<?> future = this.matchMemberJoinedEventKafkaTemplate.send(
        matchMemberJoinedTopic, key, matchMemberJoinedEvent);

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

  /**
   * 방 멤버 취소 이벤트를 발행
   */
  public void sendMatchMemberLeftEvent(MatchMemberCancelledEvent matchMemberCancelledEvent) {
    String key = String.valueOf(matchMemberCancelledEvent.roomId());

    CompletableFuture<?> future = this.matchMemberCanceledEventKafkaTemplate.send(
        matchMemberCanceledTopic, key, matchMemberCancelledEvent);

    future.thenAccept(result -> {
          if (result instanceof RecordMetadata metadata) {
            log.info("[KAFKA PRODUCER] Success sending MatchMemberLeftEvent: "
                    + "topic={}, partition={}, offset={}, key={}",
                metadata.topic(), metadata.partition(), metadata.offset(), key
            );
          }
        }
    ).exceptionally(ex -> {
      log.error("[KAFKA PRODUCER] Failed to send MatchMemberLeftEvent key={}", key, ex);
      return null;
    });
  }

  /**
   * 방 취소 이벤트를 발행
   */
  public void sendMatchRoomCancelledEvent(MatchRoomCancelledEvent matchRoomCancelledEvent) {
    String key = String.valueOf(matchRoomCancelledEvent.roomId());

    CompletableFuture<?> future = this.matchRoomCancelledEventKafkaTemplate.send(
        matchRoomCancelledTopic, key, matchRoomCancelledEvent);

    future.thenAccept(result -> {
          if (result instanceof RecordMetadata metadata) {
            log.info("[KAFKA PRODUCER] Success sending MatchRoomCancelledEvent: "
                    + "topic={}, partition={}, offset={}, key={}",
                metadata.topic(), metadata.partition(), metadata.offset(), key
            );
          }
        }
    ).exceptionally(ex -> {
      log.error("[KAFKA PRODUCER] Failed to send MatchRoomCancelledEvent key={}", key, ex);
      return null;
    });
  }

  /**
   * 매칭 성공 이벤트를 발행
   */
  public void sendMatchRoomCompletedEvent(MatchRoomCompletedEvent matchRoomCompletedEvent) {
    String key = String.valueOf(matchRoomCompletedEvent.roomId());

    CompletableFuture<?> future = this.matchRoomCompletedEventKafkaTemplate.send(
        matchRoomCancelledTopic, key, matchRoomCompletedEvent);

    future.thenAccept(result -> {
          if (result instanceof RecordMetadata metadata) {
            log.info("[KAFKA PRODUCER] Success sending MatchRoomCompleted: "
                    + "topic={}, partition={}, offset={}, key={}",
                metadata.topic(), metadata.partition(), metadata.offset(), key
            );
          }
        }
    ).exceptionally(ex -> {
      log.error("[KAFKA PRODUCER] Failed to send MatchRoomCompleted key={}", key, ex);
      return null;
    });
  }
}