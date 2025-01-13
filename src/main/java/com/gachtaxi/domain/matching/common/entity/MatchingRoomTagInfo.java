package com.gachtaxi.domain.matching.common.entity;

import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matching_room_tag_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MatchingRoomTagInfo extends BaseEntity {

  @ManyToOne
  private MatchingRoom matchingRoom;

  @Enumerated(EnumType.STRING)
  private Tags tags;
}
