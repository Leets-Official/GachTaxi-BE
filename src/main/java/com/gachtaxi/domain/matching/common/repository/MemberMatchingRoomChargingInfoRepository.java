package com.gachtaxi.domain.matching.common.repository;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.MemberMatchingRoomChargingInfo;
import com.gachtaxi.domain.matching.common.entity.enums.PaymentStatus;
import com.gachtaxi.domain.members.entity.Members;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMatchingRoomChargingInfoRepository extends JpaRepository<MemberMatchingRoomChargingInfo, Long> {

  List<MemberMatchingRoomChargingInfo> findByMatchingRoomAndPaymentStatus(MatchingRoom matchingRoom, PaymentStatus paymentStatus);
  Optional<MemberMatchingRoomChargingInfo> findByMembersAndMatchingRoom(Members members, MatchingRoom matchingRoom);

  int countByMatchingRoomAndPaymentStatus(MatchingRoom matchingRoom, PaymentStatus paymentStatus);
}
