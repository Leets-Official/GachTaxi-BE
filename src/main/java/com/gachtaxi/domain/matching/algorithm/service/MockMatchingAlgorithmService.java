package com.gachtaxi.domain.matching.algorithm.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.common.exception.PageNotFoundException;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.MemberNotFoundException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockMatchingAlgorithmService implements MatchingAlgorithmService {

  private final MatchingRoomRepository matchingRoomRepository;
  private final MemberRepository memberRepository;

  @Override
  public Optional<FindRoomResult> findRoom(Long userId, double startLongitude, double startLatitude, double destinationLongitude, double destinationLatitude,
                                           List<Tags> criteria) {
    /*
     사용자 ID로 사용자 정보 조회(이미 방에 참여하고 있는지 중복체크)
     */
    Members user = memberRepository.findById(userId)
            .orElseThrow(MemberNotFoundException::new);

    boolean isAlreadyInRoom = matchingRoomRepository.existsByMemberInMatchingRoom(user);
    if (isAlreadyInRoom) {
      return Optional.empty();
    }
    /*
     출발지 기준 300m 이내 + 태그 조건에 맞는 방 검색
     */
    List<MatchingRoom> matchingRooms = matchingRoomRepository.findRoomsByStartAndDestinationAndTags(
            startLongitude,
            startLatitude,
            destinationLongitude,
            destinationLatitude,
            criteria
    );
    /*
     조건에 맞는 방이 있으면 첫 번째 방의 상세 정보 반환
     */
    if (!matchingRooms.isEmpty()) {
      MatchingRoom room = matchingRooms.get(0);
      return Optional.of(
              FindRoomResult.builder()
                      .roomId(room.getId())
                      .maxCapacity(room.getCapacity())
                      .build()
      );
    }
    /*
     조건에 맞는 방이 없으면 empty 반환
     */
    return Optional.empty();
  }
  @Override
  public Page<MatchingRoom> findMatchingRooms(int pageNumber, int pageSize) {

    if (pageNumber < 0) {
      throw new PageNotFoundException();
    }

    PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

    return matchingRoomRepository.findAll(pageable);
  }

}
