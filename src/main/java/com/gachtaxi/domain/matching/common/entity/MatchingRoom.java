package com.gachtaxi.domain.matching.common.entity;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matching_room")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchingRoom extends BaseEntity {

  @OneToMany(mappedBy = "matchingRoom")
  private List<MatchingRoomTagInfo> matchingRoomTagInfo;

  @Column(name = "capacity", nullable = false, columnDefinition = "INT CHECK (capacity BETWEEN 1 AND 4)")
  @Getter
  private Integer capacity;

  // 팀원들 정보
  @OneToMany(mappedBy = "matchingRoom", fetch = FetchType.LAZY)
  private List<MemberMatchingRoomChargingInfo> memberMatchingRoomChargingInfo;

  @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
  private Members roomMaster;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", nullable = false)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  private Route route;

  @Column(name = "total_charge")
  @Getter
  private Integer totalCharge;

  @Enumerated(EnumType.STRING)
  private MatchingRoomStatus matchingRoomStatus;

  public boolean isActiveMatchingRoom() {
    return this.matchingRoomStatus == MatchingRoomStatus.ACTIVE;
  }
  public boolean containsTag(Tags tag) {
    return this.matchingRoomTagInfo.stream()
            .anyMatch(tagInfo -> tagInfo.matchesTag(tag));
  }
  public FindRoomResult toFindRoomResult() {
    return FindRoomResult.builder()
            .roomId(this.getId())
            .maxCapacity(this.getCapacity())
            .build();
  }
}
