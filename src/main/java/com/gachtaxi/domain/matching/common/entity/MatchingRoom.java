package com.gachtaxi.domain.matching.common.entity;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomType;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
  @Getter
  private List<MemberMatchingRoomChargingInfo> memberMatchingRoomChargingInfo;

  @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
  @Getter
  @Setter
  private Members roomMaster;

  @Column(name = "title")
  @Getter
  private String title;

  @Column(name = "description", nullable = false)
  @Getter
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  private Route route;

  @Column(name = "total_charge")
  @Getter
  private Integer totalCharge;

  @Column(name = "departure_time")
  @Getter
  private LocalDateTime departureTime;

  @Column(name = "departure")
  @Getter
  private String departure;

  @Column(name = "destination")
  @Getter
  private String destination;

  @Column(name = "chatting_room_id")
  @Getter
  private Long chattingRoomId;

  @Enumerated(EnumType.STRING)
  private MatchingRoomStatus matchingRoomStatus;

  @Enumerated(EnumType.STRING)
  private MatchingRoomType matchingRoomType;

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

  public void convertToAutoMatching() { this.matchingRoomType = MatchingRoomType.AUTO; }

  public boolean isAutoConvertible(int currentMembers) { return currentMembers < this.capacity; }

  public static MatchingRoom activeOf(MatchRoomCreatedEvent matchRoomCreatedEvent, Members members, Route route) {
    return MatchingRoom.builder()
        .capacity(matchRoomCreatedEvent.maxCapacity())
        .roomMaster(members)
        .title(matchRoomCreatedEvent.title())
        .description(matchRoomCreatedEvent.description())
        .route(route)
        .totalCharge(matchRoomCreatedEvent.expectedTotalCharge())
        .matchingRoomStatus(MatchingRoomStatus.ACTIVE)
        .build();
  }

  public static MatchingRoom manualOf(Members roomMaster, String departure, String destination, String description, int maxCapacity, int totalCharge, LocalDateTime departureTime, Long chattingRoomId) {
    return MatchingRoom.builder()
            .capacity(4)
            .roomMaster(roomMaster)
            .description(description)
            .departure(departure)
            .destination(destination)
            .totalCharge(totalCharge)
            .departureTime(departureTime)
            .chattingRoomId(chattingRoomId)
            .matchingRoomType(MatchingRoomType.MANUAL)
            .matchingRoomStatus(MatchingRoomStatus.ACTIVE)
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
            .build();
  }
  public int getCurrentMemberCount() {
    return (int) memberMatchingRoomChargingInfo.stream()
            .filter(info -> !info.isAlreadyLeft())
            .count();
  }

  public List<String> getTags() {
    return this.matchingRoomTagInfo.stream()
            .map(tagInfo -> tagInfo.getTags().name())
            .toList();
  }
}
