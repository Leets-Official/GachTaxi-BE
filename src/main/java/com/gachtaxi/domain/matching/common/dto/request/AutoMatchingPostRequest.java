package com.gachtaxi.domain.matching.common.dto.request;

import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import java.util.List;

public record AutoMatchingPostRequest(
//    String startPoint,
//    String startName,
//    String destinationPoint,
//    String destinationName,
    String departure,
    String destination,
    List<String> criteria,
    List<Integer> members,
    Integer expectedTotalCharge
) {

  public List<Tags> getCriteria() {
    return this.criteria.stream()
        .map(Tags::valueOf)
        .toList();
  }
}
