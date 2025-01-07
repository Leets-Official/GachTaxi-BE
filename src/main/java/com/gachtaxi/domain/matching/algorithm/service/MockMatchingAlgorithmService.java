package com.gachtaxi.domain.matching.algorithm.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockMatchingAlgorithmService implements MatchingAlgorithmService {

  private final MatchingRoomRepository matchingRoomRepository;

  @Override
  public Optional<FindRoomResult> findRoom(Long userId, List<Tags> criteria) {
    List<MatchingRoom> matchingRoomList = this.matchingRoomRepository.findAll();
    if (!matchingRoomList.isEmpty()) {
      MatchingRoom matchingRoom = matchingRoomList.get(0);
      return Optional.of(
          FindRoomResult.builder()
              .roomId(matchingRoom.getId())
              .maxCapacity(matchingRoom.getCapacity())
              .build());
    }
    return Optional.empty();
  }
}
