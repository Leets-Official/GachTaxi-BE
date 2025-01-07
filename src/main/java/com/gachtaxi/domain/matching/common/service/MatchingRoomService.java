package com.gachtaxi.domain.matching.common.service;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.MatchingRoomTagInfo;
import com.gachtaxi.domain.matching.common.entity.MemberMatchingRoomChargingInfo;
import com.gachtaxi.domain.matching.common.entity.Route;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.common.exception.NoSuchMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.NotActiveMatchingRoomException;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomTagInfoRepository;
import com.gachtaxi.domain.matching.common.repository.MemberMatchingRoomChargingInfoRepository;
import com.gachtaxi.domain.matching.common.repository.RouteRepository;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.service.MemberService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingRoomService {

  // service
  private final MemberService memberService;

  // repository
  private final MatchingRoomRepository matchingRoomRepository;
  private final MatchingRoomTagInfoRepository matchingRoomTagInfoRepository;
  private final RouteRepository routeRepository;
  private final MemberMatchingRoomChargingInfoRepository memberMatchingRoomChargingInfoRepository;

  public MatchingRoom save(MatchRoomCreatedEvent matchRoomCreatedEvent) {
    Members members = this.memberService.findById(matchRoomCreatedEvent.hostMemberId());

    Route route = this.saveRoute(matchRoomCreatedEvent);

    MatchingRoom matchingRoom = MatchingRoom.builder()
        .capacity(matchRoomCreatedEvent.maxCapacity())
        .roomMaster(members)
        .title(matchRoomCreatedEvent.title())
        .description(matchRoomCreatedEvent.description())
        .route(route)
        .totalCharge(matchRoomCreatedEvent.expectedTotalCharge())
        .matchingRoomStatus(MatchingRoomStatus.ACTIVE)
        .build();

    this.saveMatchingRoomTagInfo(matchingRoom, matchRoomCreatedEvent.criteria());
    this.saveHostMemberChargingInfo(matchingRoom, members);

    return this.matchingRoomRepository.save(matchingRoom);
  }

  private Route saveRoute(MatchRoomCreatedEvent matchRoomCreatedEvent) {
    Route route = Route.builder()
        .startLocationCoordinate(matchRoomCreatedEvent.startPoint())
        .startLocationName(matchRoomCreatedEvent.startName())
        .endLocationCoordinate(matchRoomCreatedEvent.destinationPoint())
        .endLocationName(matchRoomCreatedEvent.destinationName())
        .build();

    return this.routeRepository.save(route);
  }

  private void saveMatchingRoomTagInfo(MatchingRoom matchingRoom, List<Tags> tags) {
    for (Tags tag : tags) {
      MatchingRoomTagInfo matchingRoomTagInfo = MatchingRoomTagInfo.builder()
          .matchingRoom(matchingRoom)
          .tags(tag)
          .build();

      this.matchingRoomTagInfoRepository.save(matchingRoomTagInfo);
    }
  }

  private void saveHostMemberChargingInfo(MatchingRoom matchingRoom, Members members) {
    MemberMatchingRoomChargingInfo matchingRoomChargingInfo = MemberMatchingRoomChargingInfo.builder()
        .matchingRoom(matchingRoom)
        .members(members)
        .charge(matchingRoom.getTotalCharge())
        .build();

    this.memberMatchingRoomChargingInfoRepository.save(matchingRoomChargingInfo);
  }

  public void joinMemberToMatchingRoom(MatchMemberJoinedEvent matchMemberJoinedEvent) {
    Members members = this.memberService.findById(matchMemberJoinedEvent.memberId());
    MatchingRoom matchingRoom = this.matchingRoomRepository.findById(
        matchMemberJoinedEvent.roomId()).orElseThrow(
        NoSuchMatchingRoomException::new);

    if (!matchingRoom.isActiveMatchingRoom()) {
      throw new NotActiveMatchingRoomException();
    }

    List<MemberMatchingRoomChargingInfo> existMembers = this.memberMatchingRoomChargingInfoRepository.findByMatchingRoom(
        matchingRoom);

    // TODO: 딱 떨어지지 않는 금액은 어떻게 해야할지?
    int distributedCharge = matchingRoom.getTotalCharge() / (existMembers.size() + 1);

    this.memberMatchingRoomChargingInfoRepository.save(
        MemberMatchingRoomChargingInfo.builder()
            .matchingRoom(matchingRoom)
            .members(members)
            .charge(distributedCharge)
            .build()
    );

    this.updateExistMembersCharge(existMembers, distributedCharge);
  }

  private void updateExistMembersCharge(List<MemberMatchingRoomChargingInfo> existMembers, int charge) {
    for (MemberMatchingRoomChargingInfo memberMatchingRoomChargingInfo : existMembers) {
      memberMatchingRoomChargingInfo.setCharge(charge);
    }
    this.memberMatchingRoomChargingInfoRepository.saveAll(existMembers);
  }
}
