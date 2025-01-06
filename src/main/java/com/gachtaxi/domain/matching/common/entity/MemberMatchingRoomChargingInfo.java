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
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "member_matching_room_charging_info",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"members_id", "matching_room_id"})
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberMatchingRoomChargingInfo extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  private Members members;

  @ManyToOne(fetch = FetchType.LAZY)
  private MatchingRoom matchingRoom;

  @Column(name = "charge")
  private Integer charge;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus = PaymentStatus.NOT_PAYED;
}
