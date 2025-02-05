package com.gachtaxi.domain.matching.algorithm.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.common.exception.AlreadyInMatchingRoomException;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.service.BlacklistService;
import com.gachtaxi.domain.members.service.MemberService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingAlgorithmServiceImpl implements MatchingAlgorithmService {

  private final MatchingRoomRepository matchingRoomRepository;
  private final MemberService memberService;
  private final BlacklistService blacklistService;

  @Value("${gachtaxi.matching.auto-matching-description}")
  private String autoMatchingDescription;

//  private static final double SEARCH_RADIUS = 300.0;

  @Override
  public Optional<FindRoomResult> findRoom(Long userId, String departure, String destination,
      List<Tags> criteria) {
    /*
     사용자 ID로 사용자 정보 조회(이미 방에 참여하고 있는지 중복체크)
     */
    Members user = memberService.findById(userId);

    matchingRoomRepository.findByMemberInMatchingRoom(user)
        .forEach(room -> {
          if (room.getDescription().equals(autoMatchingDescription)) {
            throw new AlreadyInMatchingRoomException(room.getChattingRoomId());
          }
        });

      /*
        출발지와 도착지 기준으로 방 검색
      */
      List<MatchingRoom> matchingRooms = matchingRoomRepository.findRoomsByDepartureAndDestination(departure, destination);

//    /*
//     위치 정보를 이용한 방 검색(300M 이내)ø
//     */
//    List<MatchingRoom> matchingRooms = matchingRoomRepository.findRoomsByStartAndDestination(
//        startLongitude,
//        startLatitude,
//        destinationLongitude,
//        destinationLatitude,
//        SEARCH_RADIUS
//    );
    /*
      ACTIVE 상태인 방 && 블랙리스트가 없는 방만 필터링
     */
    matchingRooms = matchingRooms.stream()
        .filter(MatchingRoom::isActive)
        .filter(room -> !this.blacklistService.isBlacklistInMatchingRoom(user, room))
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
}
