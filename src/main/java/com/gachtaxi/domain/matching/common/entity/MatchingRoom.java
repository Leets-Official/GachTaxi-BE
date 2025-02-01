package com.gachtaxi.domain.matching.common.entity;

import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matching_room")
@Builder(access = AccessLevel.PRIVATE)
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
  @Getter
  @Setter
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

  @OneToOne
  @Getter
  private ChattingRoom chattingRoom;

  public boolean isActive() {
    return this.matchingRoomStatus == MatchingRoomStatus.ACTIVE;
  }

  public void changeRoomMaster(Members members) {
    this.setRoomMaster(members);
  }

  public void cancelMatchingRoom() {
    this.matchingRoomStatus = MatchingRoomStatus.CANCELLED;
  }

  public void completeMatchingRoom() {
    this.matchingRoomStatus = MatchingRoomStatus.COMPLETE;
  }

  public boolean isFull(int size) {
    return size == totalCharge;
  }

  public static MatchingRoom activeOf(MatchRoomCreatedEvent matchRoomCreatedEvent, Members members, Route route, ChattingRoom chattingRoom) {
    return MatchingRoom.builder()
        .capacity(matchRoomCreatedEvent.maxCapacity())
        .roomMaster(members)
        .title(matchRoomCreatedEvent.title())
        .description(matchRoomCreatedEvent.description())
        .route(route)
        .totalCharge(matchRoomCreatedEvent.expectedTotalCharge())
        .matchingRoomStatus(MatchingRoomStatus.ACTIVE)
        .chattingRoom(chattingRoom)
        .build();
  }
  public boolean containsTag(Tags tag) {
    return this.matchingRoomTagInfo.stream()
            .anyMatch(tagInfo -> tagInfo.matchesTag(tag));
  }
  public FindRoomResult toFindRoomResult() {
    return FindRoomResult.builder()
            .roomId(this.getId())
            .maxCapacity(this.getCapacity())
            .chattingRoomId(this.getChattingRoom().getId())
            .build();
  }
}
