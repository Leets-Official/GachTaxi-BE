package com.gachtaxi.domain.matching.common.service;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.MatchingRoomTagInfo;
import com.gachtaxi.domain.matching.common.entity.MemberMatchingRoomChargingInfo;
import com.gachtaxi.domain.matching.common.entity.Route;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomTagInfoRepository;
import com.gachtaxi.domain.matching.common.repository.MemberMatchingRoomChargingInfoRepository;
import com.gachtaxi.domain.matching.common.repository.RouteRepository;
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
        .totalCharge(matchRoomCreatedEvent.totalCharge())
        .matchingRoomStatus(MatchingRoomStatus.ACTIVE)
        .build();

    this.saveMatchingRoomTagInfo(matchingRoom, matchRoomCreatedEvent.criteria());

    return this.matchingRoomRepository.save(matchingRoom);
  }

  public Route saveRoute(MatchRoomCreatedEvent matchRoomCreatedEvent) {
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
}
