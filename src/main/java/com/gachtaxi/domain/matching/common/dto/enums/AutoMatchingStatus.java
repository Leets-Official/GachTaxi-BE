package com.gachtaxi.domain.matching.common.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AutoMatchingStatus {
  REQUESTED("REQUESTED"),
  REJECTED("REJECTED"),
  CANCELLED("CANCELLED");
  private final String value;
}
