package com.gachtaxi.domain.matching.algorithm.dto;

import lombok.Builder;

@Builder
public record FindRoomResult(
    Long roomId,
//    String roomType,
    Integer currentMembers,
    Integer maxCapacity
) {

}
