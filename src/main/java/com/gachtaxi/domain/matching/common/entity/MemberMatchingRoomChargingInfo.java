package com.gachtaxi.domain.matching.common.entity;

import com.gachtaxi.domain.matching.common.entity.enums.PaymentStatus;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "member_matching_room_charging_info",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"members_id", "matching_room_id"})
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class MemberMatchingRoomChargingInfo extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @Getter
  private Members members;

  @ManyToOne(fetch = FetchType.LAZY)
  private MatchingRoom matchingRoom;

  @Column(name = "charge")
  @Setter
  private Integer charge;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  public void leftMatchingRoom() {
    this.paymentStatus = PaymentStatus.LEFT;
  }

  public boolean isAlreadyLeft() {
    return this.paymentStatus == PaymentStatus.LEFT;
  }

  public MemberMatchingRoomChargingInfo joinMatchingRoom() {
    this.paymentStatus = PaymentStatus.NOT_PAYED;
    return this;
  }

  public static MemberMatchingRoomChargingInfo notPayedOf(MatchingRoom matchingRoom, Members members) {
    return MemberMatchingRoomChargingInfo.builder()
        .matchingRoom(matchingRoom)
        .members(members)
        .charge(matchingRoom.getTotalCharge())
        .paymentStatus(PaymentStatus.NOT_PAYED)
        .build();
  }
}
