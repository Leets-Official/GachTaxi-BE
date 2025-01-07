package com.gachtaxi.domain.matching.algorithm.dto;

import lombok.Builder;

@Builder
public record FindRoomResult(
    Long roomId,
    Integer currentMembers,
    Integer maxCapacity
) {

}
