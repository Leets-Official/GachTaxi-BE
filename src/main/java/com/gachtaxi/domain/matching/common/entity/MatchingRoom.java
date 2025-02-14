package com.gachtaxi.domain.matching.common.entity;

import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomType;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
  private String departureTime;

  @Column(name = "departure_date")
  @Getter
  private String departureDate;

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

  public static MatchingRoom activeOf(MatchRoomCreatedEvent matchRoomCreatedEvent, Members members, ChattingRoom chattingRoom) {
    return MatchingRoom.builder()
        .capacity(matchRoomCreatedEvent.maxCapacity())
        .roomMaster(members)
        .title(matchRoomCreatedEvent.title())
        .description(matchRoomCreatedEvent.description())
//        .route(route)
        .departure(matchRoomCreatedEvent.startName())
        .destination(matchRoomCreatedEvent.destinationName())
        .totalCharge(matchRoomCreatedEvent.expectedTotalCharge())
        .matchingRoomType(MatchingRoomType.AUTO)
        .matchingRoomStatus(MatchingRoomStatus.ACTIVE)
        .chattingRoomId(chattingRoom.getId())
        .build();
  }

  public static MatchingRoom manualOf(Members roomMaster, String departure, String destination, String description, int maxCapacity, int totalCharge, String departureTime, String departureDate,Long chattingRoomId) {
    return MatchingRoom.builder()
            .capacity(4)
            .roomMaster(roomMaster)
            .description(description)
            .departure(departure)
            .destination(destination)
            .totalCharge(totalCharge)
            .departureTime(departureTime)
            .departureDate(departureDate)
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
            .chattingRoomId(this.chattingRoomId)
            .currentMembers(this.getCurrentMemberCount())
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
