package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingPostRequest;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Builder(access = AccessLevel.PRIVATE)
public record MatchRoomCreatedEvent(
    Long roomMasterId,
    Integer maxCapacity,
    String title,
    String description,
//    String startPoint,
    String startName,
//    String destinationPoint,
    String destinationName,
    List<Tags> criteria,
    Integer expectedTotalCharge,
    Long roomId,
    Long chattingRoomId,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    String topic
) implements MatchingEvent{

  @Override
  public Object getKey() {
    return null;
  }

  @Override
  public String getTopic() {
    return this.topic;
  }

  public static MatchRoomCreatedEvent of(MatchRoomCreatedEvent event, Long roomId, Long chattingRoomId) {
    return MatchRoomCreatedEvent.builder()
        .roomMasterId(event.roomMasterId())
        .maxCapacity(event.maxCapacity())
        .title(event.title())
        .description(event.description())
//        .startPoint(event.startPoint())
        .startName(event.startName())
//        .destinationPoint(event.destinationPoint())
        .destinationName(event.destinationName())
        .criteria(event.criteria())
        .expectedTotalCharge(event.expectedTotalCharge())
        .roomId(roomId)
        .createdAt(event.createdAt())
        .topic(event.topic())
        .chattingRoomId(chattingRoomId)
        .build();
  }

  public static MatchRoomCreatedEvent of(
      Long roomMasterId,
      AutoMatchingPostRequest autoMatchingPostRequest,
      int maxCapacity,
      String description,
      String topic
      ) {
    return MatchRoomCreatedEvent.builder()
        .roomMasterId(roomMasterId)
//        .startPoint(autoMatchingPostRequest.startPoint())
//        .startName(autoMatchingPostRequest.startName())
//        .destinationPoint(autoMatchingPostRequest.destinationPoint())
//        .destinationName(autoMatchingPostRequest.destinationName())
        .startName(autoMatchingPostRequest.departure())
        .destinationName(autoMatchingPostRequest.destination())
        .maxCapacity(maxCapacity)
        .title(UUID.randomUUID().toString())
        .description(description)
        .expectedTotalCharge(autoMatchingPostRequest.expectedTotalCharge())
        .criteria(autoMatchingPostRequest.getCriteria())
        .topic(topic)
        .build();
  }
}
