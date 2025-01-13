package com.gachtaxi.domain.matching.common.entity;

import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "route")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Route extends BaseEntity {

  private String startLocationCoordinate;
  private String startLocationName;

  private String endLocationCoordinate;
  private String endLocationName;

  public static Route from(MatchRoomCreatedEvent matchRoomCreatedEvent) {
    return Route.builder()
        .startLocationCoordinate(matchRoomCreatedEvent.startPoint())
        .startLocationName(matchRoomCreatedEvent.startName())
        .endLocationCoordinate(matchRoomCreatedEvent.destinationPoint())
        .endLocationName(matchRoomCreatedEvent.destinationName())
        .build();
  }
}
