package com.gachtaxi.domain.matching.common.dto.request;

import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AutoMatchingPostRequest(
    String startPoint,
//    String startName,
    String destinationPoint,
//    String destinationName,

    // 현재 사용하는 필드
    String startName,
    String destinationName,
    List<String> criteria,
    @NotNull
    List<Long> members,

    @Min(value = 4000)
    int expectedTotalCharge
) {

  public List<Tags> getCriteria() {
    return this.criteria.stream()
        .map(Tags::valueOf)
        .toList();
  }
  public String getDeparture() {
    return startName;
  }
  public String getDestination() {
    return destinationName;
  }
}
