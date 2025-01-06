package com.gachtaxi.domain.matching.event.dto.kafka_topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record MatchRoomCreatedEvent(
    Long hostMemberId,
    Integer maxCapacity,
    String title,
    String description,
    Integer totalCharge,
    String startPoint,
    String startName,
    String destinationPoint,
    String destinationName,
    List<Tags> criteria,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

}
