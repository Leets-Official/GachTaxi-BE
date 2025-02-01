package com.gachtaxi.domain.matching.common.service;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.MatchingRoomTagInfo;
import com.gachtaxi.domain.matching.common.entity.MemberMatchingRoomChargingInfo;
import com.gachtaxi.domain.matching.common.entity.Route;
import com.gachtaxi.domain.matching.common.entity.enums.PaymentStatus;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.common.exception.MemberAlreadyLeftMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.MemberNotInMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.NoSuchMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.NotActiveMatchingRoomException;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomTagInfoRepository;
import com.gachtaxi.domain.matching.common.repository.MemberMatchingRoomChargingInfoRepository;
import com.gachtaxi.domain.matching.common.repository.RouteRepository;
import com.gachtaxi.domain.matching.event.MatchingEventFactory;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCompletedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import com.gachtaxi.domain.matching.event.service.kafka.AutoMatchingProducer;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.service.MemberService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MatchingRoomService {

  // service
  private final MemberService memberService;
  private final AutoMatchingProducer autoMatchingProducer;

  // repository
  private final MatchingRoomRepository matchingRoomRepository;
  private final MatchingRoomTagInfoRepository matchingRoomTagInfoRepository;
  private final RouteRepository routeRepository;
  private final MemberMatchingRoomChargingInfoRepository memberMatchingRoomChargingInfoRepository;

  // event factory
  private final MatchingEventFactory matchingEventFactory;

  public Long createMatchingRoom(MatchRoomCreatedEvent matchRoomCreatedEvent) {
    Members members = this.memberService.findById(matchRoomCreatedEvent.roomMasterId());

    Route route = this.saveRoute(matchRoomCreatedEvent);

    MatchingRoom matchingRoom = MatchingRoom.activeOf(matchRoomCreatedEvent, members, route);

    this.saveMatchingRoomTagInfo(matchingRoom, matchRoomCreatedEvent.criteria());
    this.saveRoomMasterChargingInfo(matchingRoom, members);

    MatchingRoom savedMatchingRoom = this.matchingRoomRepository.save(matchingRoom);

    return savedMatchingRoom.getId();
  }

  private Route saveRoute(MatchRoomCreatedEvent matchRoomCreatedEvent) {
    String[] startCoordinates = matchRoomCreatedEvent.startPoint().split(",");
    double startLatitude = Double.parseDouble(startCoordinates[0]);
    double startLongitude = Double.parseDouble(startCoordinates[1]);

    String[] endCoordinates = matchRoomCreatedEvent.destinationPoint().split(",");
    double endLatitude = Double.parseDouble(endCoordinates[0]);
    double endLongitude = Double.parseDouble(endCoordinates[1]);

    Route route = Route.builder()
            .startLongitude(startLongitude)
            .startLatitude(startLatitude)
            .startLocationName(matchRoomCreatedEvent.startName())
            .endLongitude(endLongitude)
            .endLatitude(endLatitude)
            .endLocationName(matchRoomCreatedEvent.destinationName())
            .build();
    return this.routeRepository.save(route);
  }

  private void saveMatchingRoomTagInfo(MatchingRoom matchingRoom, List<Tags> tags) {
    tags.forEach(tag -> this.matchingRoomTagInfoRepository.save(MatchingRoomTagInfo.of(matchingRoom, tag)));
  }

  private void saveRoomMasterChargingInfo(MatchingRoom matchingRoom, Members members) {
    this.memberMatchingRoomChargingInfoRepository.save(MemberMatchingRoomChargingInfo.notPayedOf(matchingRoom, members));
  }

  public void joinMemberToMatchingRoom(MatchMemberJoinedEvent matchMemberJoinedEvent) {
    Members members = this.memberService.findById(matchMemberJoinedEvent.memberId());
    MatchingRoom matchingRoom = this.matchingRoomRepository.findById(matchMemberJoinedEvent.roomId()).orElseThrow(NoSuchMatchingRoomException::new);

    if (!matchingRoom.isActive()) {
      throw new NotActiveMatchingRoomException();
    }

    MemberMatchingRoomChargingInfo requestedMembersInfo = null;

    Optional<MemberMatchingRoomChargingInfo> joinedInPast = this.alreadyJoinedInPast(members, matchingRoom);

    if (joinedInPast.isPresent()) {
      requestedMembersInfo = joinedInPast.get().joinMatchingRoom();
    } else {
      requestedMembersInfo = MemberMatchingRoomChargingInfo.notPayedOf(matchingRoom, members);
    }
    this.memberMatchingRoomChargingInfoRepository.save(requestedMembersInfo);

    List<MemberMatchingRoomChargingInfo> existMembers = this.memberMatchingRoomChargingInfoRepository.findByMatchingRoomAndPaymentStatus(matchingRoom, PaymentStatus.NOT_PAYED);

    int distributedCharge = (int) Math.ceil((double) matchingRoom.getTotalCharge() / (existMembers.size() + 1));

    this.updateExistMembersCharge(existMembers, distributedCharge);

    int nowMemberCount = existMembers.size() + 1;

    if (matchingRoom.isFull(nowMemberCount)) {
      this.autoMatchingProducer.sendEvent(this.matchingEventFactory.createMatchRoomCompletedEvent(matchingRoom.getId()));
    }
  }

  private Optional<MemberMatchingRoomChargingInfo> alreadyJoinedInPast(Members members, MatchingRoom matchingRoom) {
    return this.memberMatchingRoomChargingInfoRepository.findByMembersAndMatchingRoom(members, matchingRoom);
  }

  private void updateExistMembersCharge(List<MemberMatchingRoomChargingInfo> existMembers, int charge) {
    for (MemberMatchingRoomChargingInfo memberMatchingRoomChargingInfo : existMembers) {
      memberMatchingRoomChargingInfo.setCharge(charge);
    }
    this.memberMatchingRoomChargingInfoRepository.saveAll(existMembers);
  }

  public void leaveMemberFromMatchingRoom(MatchMemberCancelledEvent matchMemberCancelledEvent) {
    Members members = this.memberService.findById(matchMemberCancelledEvent.memberId());
    MatchingRoom matchingRoom = this.matchingRoomRepository.findById(matchMemberCancelledEvent.roomId()).orElseThrow(NoSuchMatchingRoomException::new);

    MemberMatchingRoomChargingInfo memberMatchingRoomChargingInfo =
        this.memberMatchingRoomChargingInfoRepository.findByMembersAndMatchingRoom(members, matchingRoom)
        .orElseThrow(MemberNotInMatchingRoomException::new);

    if (memberMatchingRoomChargingInfo.isAlreadyLeft()) {
      throw new MemberAlreadyLeftMatchingRoomException();
    }

    memberMatchingRoomChargingInfo.leftMatchingRoom();

    this.memberMatchingRoomChargingInfoRepository.save(memberMatchingRoomChargingInfo);

    if (members.isRoomMaster(matchingRoom)) {
      this.findNextRoomMaster(matchingRoom, members)
          .ifPresentOrElse(
              nextRoomMaster -> matchingRoom.changeRoomMaster(nextRoomMaster),
              () -> this.autoMatchingProducer.sendEvent(this.matchingEventFactory.createMatchRoomCancelledEvent(matchingRoom.getId()))
          );
    }
  }

  private Optional<Members> findNextRoomMaster(MatchingRoom matchingRoom, Members members) {
    List<MemberMatchingRoomChargingInfo> existMembers =
        this.memberMatchingRoomChargingInfoRepository.findByMatchingRoomAndPaymentStatus(matchingRoom, PaymentStatus.NOT_PAYED);

    return existMembers.stream()
        .map(MemberMatchingRoomChargingInfo::getMembers)
        .filter(member -> !member.equals(members))
        .findFirst();
  }

  public void cancelMatchingRoom(MatchRoomCancelledEvent matchRoomCancelledEvent) {
    MatchingRoom matchingRoom = this.getMatchingRoomById(matchRoomCancelledEvent.roomId());

    if (!matchingRoom.isActive()) {
      throw new NotActiveMatchingRoomException();
    }

    matchingRoom.cancelMatchingRoom();
    this.matchingRoomRepository.save(matchingRoom);
  }

  public void completeMatchingRoom(MatchRoomCompletedEvent matchRoomCompletedEvent) {
    MatchingRoom matchingRoom = this.getMatchingRoomById(matchRoomCompletedEvent.roomId());

    if (!matchingRoom.isActive()) {
      throw new NotActiveMatchingRoomException();
    }

    matchingRoom.completeMatchingRoom();
    this.matchingRoomRepository.save(matchingRoom);
  }

  private MatchingRoom getMatchingRoomById(Long roomId) {
    return this.matchingRoomRepository.findById(roomId).orElseThrow(NoSuchMatchingRoomException::new);
  }
}
