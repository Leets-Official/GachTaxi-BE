package com.gachtaxi.domain.matching.algorithm.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockMatchingAlgorithmService implements MatchingAlgorithmService {


  @Override
  public Optional<FindRoomResult> findRoom(Long userId, List<Tags> criteria) {
    if (userId == 1L) {
      return Optional.of(FindRoomResult.builder()
          .roomId(1L)
          .build());
    }
    return Optional.empty();
  }

  @Override
  public Long createRoom(Long userId, List<Tags> criteria) {
    return 1L;
  }

  @Override
  public void joinRoom(Long roomId, Long userId) {

  }
}
