package com.gachtaxi.domain.matching.common.repository;

import com.gachtaxi.domain.matching.common.entity.MatchingRoomTagInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRoomTagInfoRepository extends JpaRepository<MatchingRoomTagInfo, Long> {

}
