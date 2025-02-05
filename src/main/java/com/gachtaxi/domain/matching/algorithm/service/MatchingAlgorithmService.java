package com.gachtaxi.domain.matching.algorithm.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import java.util.List;
import java.util.Optional;

public interface MatchingAlgorithmService {

  /**
   * 방을 찾는 메서드
   * 이미 방에 들어가있는 멤버가 다시 요청했을 때 Optional.empty()를 반환하도록 로직을 구성해야함
   * @param userId 방에 들어가려는 사용자 ID
   * @param departure 출발지
   * @param destination 도착지
//   * @param startLongitude 시작 지점 경도
//   * @param startLatitude 시작 지점 위도
//   * @param destinationLongitude 도착 지점 경도
//   * @param destinationLatitude 도착 지점 위도
   * @param criteria 방 검색에 필요한 기타 조건 (태그 등)
   * @return Optional<FindRoomResult> - 매칭 가능한 방 정보가 있으면 값이 있고, 없으면 empty
   */
  Optional<FindRoomResult> findRoom(Long userId, String departure, String destination, List<Tags> criteria);
}
