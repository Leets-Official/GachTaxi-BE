package com.gachtaxi.domain.matching.algorithm.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.common.exception.DuplicatedMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.PageNotFoundException;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.service.MemberService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingAlgorithmServiceImpl implements MatchingAlgorithmService {

  private final MatchingRoomRepository matchingRoomRepository;
  private final MemberService memberService;

  private static final double SEARCH_RADIUS = 300.0;

  @Override
  public Optional<FindRoomResult> findRoom(Long userId, double startLongitude, double startLatitude, double destinationLongitude, double destinationLatitude,
                                           List<Tags> criteria) {
    /*
     사용자 ID로 사용자 정보 조회(이미 방에 참여하고 있는지 중복체크)
     */
    Members user = memberService.findById(userId);

    if (matchingRoomRepository.existsByMemberInMatchingRoom(user)) {
      throw new DuplicatedMatchingRoomException(); // * 추후 논의 후 리팩토링 필요 * 똑같은 조건으로 방 생성시 예외 던져주기
    }
    /*
     위치 정보를 이용한 방 검색(300M 이내)
     */
    List<MatchingRoom> matchingRooms = matchingRoomRepository.findRoomsByStartAndDestination(
              startLongitude,
              startLatitude,
              destinationLongitude,
              destinationLatitude,
              SEARCH_RADIUS
      );
    /*
      ACTIVE 상태인 방만 필터링
     */
    matchingRooms = matchingRooms.stream()
            .filter(MatchingRoom::isActive)
            .toList();
    /*
     태그 조건이 있는 경우에 태그정보까지 필터링
     */
    if (criteria != null && !criteria.isEmpty()) {
      matchingRooms = matchingRooms.stream()
              .filter(room -> criteria.stream().anyMatch(room::containsTag))
              .toList();
    }
    /*
     조건에 맞는 방이 있으면 첫 번째 방의 상세 정보 반환
     */
    if (!matchingRooms.isEmpty()) {
      MatchingRoom room = matchingRooms.get(0);
      return Optional.of(room.toFindRoomResult());
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
