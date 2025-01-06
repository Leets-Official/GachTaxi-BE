package com.gachtaxi.domain.matching.common.repository;

import com.gachtaxi.domain.matching.common.entity.MemberMatchingRoomChargingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMatchingRoomChargingInfoRepository extends JpaRepository<MemberMatchingRoomChargingInfo, Long> {

}
