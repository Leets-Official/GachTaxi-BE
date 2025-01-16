package com.gachtaxi.domain.matching.algorithm.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MatchingAlgorithmService {

  /**
   * 방을 찾는 메서드
   * 이미 방에 들어가있는 멤버가 다시 요청했을 때 Optional.empty()를 반환하도록 로직을 구성해야함
   * @param userId 방에 들어가려는 사용자 ID
   * @param startLongitude 시작 지점 경도
   * @param startLatitude 시작 지점 위도
   * @param destinationLongitude 도착 지점 경도
   * @param destinationLatitude 도착 지점 위도
   * @param criteria 방 검색에 필요한 기타 조건 (태그 등)
   * @return Optional<FindRoomResult> - 매칭 가능한 방 정보가 있으면 값이 있고, 없으면 empty
   */
  Optional<FindRoomResult> findRoom(Long userId, double startLongitude, double startLatitude, double destinationLongitude, double destinationLatitude, List<Tags> criteria);

  /**
   * 전체 매칭 방을 페이지 단위로 조회
   *
   * @param pageNumber 페이지 번호 (0부터 시작)
   * @param pageSize   한 페이지에 포함될 매칭 방의 개수
   * @return Page<MatchingRoom> - 페이지별 매칭 방 정보
   */
  Page<MatchingRoom> findMatchingRooms(int pageNumber, int pageSize);
}
