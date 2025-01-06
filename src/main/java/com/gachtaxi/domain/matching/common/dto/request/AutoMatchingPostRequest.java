package com.gachtaxi.domain.matching.common.dto.request;

import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import java.util.List;

public record AutoMatchingPostRequest(
    String startPoint,
    String startName,
    String destinationPoint,
    String destinationName,
    List<String> criteria,
    Integer maxCapacity,
    String title,
    String description,
    Integer expectedTotalCharge
) {

  public List<Tags> getCriteria() {
    return this.criteria.stream()
        .map(Tags::valueOf)
        .toList();
  }
}
