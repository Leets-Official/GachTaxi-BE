package com.gachtaxi.domain.matching.algorithm.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import java.util.List;
import java.util.Optional;

public interface MatchingAlgorithmService {

  /**
   * 방을 찾는 메서드
   * @param userId 방에 들어가려는 사용자 ID
   * @param criteria 방 검색에 필요한 기타 조건 (ex. 위치, 키워드 등)
   * @return Optional<FindRoomResult> - 매칭 가능한 방 정보가 있으면 값이 있고, 없으면 empty
   */
  Optional<FindRoomResult> findRoom(Long userId, List<Tags> criteria);

  /**
   * 방 생성 처리 (DB에 실제 insert 등)
   * @param userId  방을 만들 사용자(방장)
   * @param criteria 조건(위치, 키워드 등)
   * @return 생성된 방의 ID
   */
  Long createRoom(Long userId, List<Tags> criteria);

  /**
   * 방에 참가 처리
   * @param roomId 참가할 방 ID
   * @param userId 참가하는 사용자
   */
  void joinRoom(Long roomId, Long userId);
}
