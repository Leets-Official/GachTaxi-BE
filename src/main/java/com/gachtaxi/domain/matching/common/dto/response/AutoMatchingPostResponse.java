package com.gachtaxi.domain.matching.common.dto.response;

import com.gachtaxi.domain.matching.common.dto.enums.AutoMatchingStatus;

public record AutoMatchingPostResponse(
    String autoMatchingStatus

) {

  public static AutoMatchingPostResponse of(AutoMatchingStatus autoMatchingStatus) {
    return new AutoMatchingPostResponse(autoMatchingStatus.getValue());
  }
}
