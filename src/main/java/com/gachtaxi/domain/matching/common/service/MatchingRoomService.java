package com.gachtaxi.domain.matching.common.service;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.MatchingRoomTagInfo;
import com.gachtaxi.domain.matching.common.entity.MemberMatchingRoomChargingInfo;
import com.gachtaxi.domain.matching.common.entity.Route;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.common.entity.enums.PaymentStatus;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.common.exception.MemberAlreadyJoinedException;
import com.gachtaxi.domain.matching.common.exception.MemberNotInMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.NoSuchMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.NotActiveMatchingRoomException;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomTagInfoRepository;
import com.gachtaxi.domain.matching.common.repository.MemberMatchingRoomChargingInfoRepository;
import com.gachtaxi.domain.matching.common.repository.RouteRepository;
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

  public MatchingRoom save(MatchRoomCreatedEvent matchRoomCreatedEvent) {
    Members members = this.memberService.findById(matchRoomCreatedEvent.roomMasterId());

    Route route = this.saveRoute(matchRoomCreatedEvent);

    MatchingRoom matchingRoom = MatchingRoom.activeFrom(matchRoomCreatedEvent, members, route);

    this.saveMatchingRoomTagInfo(matchingRoom, matchRoomCreatedEvent.criteria());
    this.saveHostMemberChargingInfo(matchingRoom, members);

    return this.matchingRoomRepository.save(matchingRoom);
  }

  private Route saveRoute(MatchRoomCreatedEvent matchRoomCreatedEvent) {
    return this.routeRepository.save(Route.from(matchRoomCreatedEvent));
  }

  private void saveMatchingRoomTagInfo(MatchingRoom matchingRoom, List<Tags> tags) {
    tags.forEach(tag -> {
      this.matchingRoomTagInfoRepository.save(MatchingRoomTagInfo.of(matchingRoom, tag));
    });
  }

  private void saveHostMemberChargingInfo(MatchingRoom matchingRoom, Members members) {
    this.memberMatchingRoomChargingInfoRepository.save(MemberMatchingRoomChargingInfo.notPayedOf(matchingRoom, members));
  }

  public void joinMemberToMatchingRoom(MatchMemberJoinedEvent matchMemberJoinedEvent) {
    Members members = this.memberService.findById(matchMemberJoinedEvent.memberId());
    MatchingRoom matchingRoom = this.matchingRoomRepository.findById(
        matchMemberJoinedEvent.roomId()).orElseThrow(
        NoSuchMatchingRoomException::new);

    if (this.memberMatchingRoomChargingInfoRepository.findByMembersAndMatchingRoom(members, matchingRoom).isPresent()) {
      throw new MemberAlreadyJoinedException();
    }

    if (!matchingRoom.isActiveMatchingRoom()) {
      throw new NotActiveMatchingRoomException();
    }

    List<MemberMatchingRoomChargingInfo> existMembers = this.memberMatchingRoomChargingInfoRepository.findByMatchingRoom(
        matchingRoom);

    // TODO: 딱 떨어지지 않는 금액은 어떻게 해야할지?
    int distributedCharge = matchingRoom.getTotalCharge() / (existMembers.size() + 1);

    this.memberMatchingRoomChargingInfoRepository.save(
        MemberMatchingRoomChargingInfo.notPayedOf(matchingRoom, members)
    );

    this.updateExistMembersCharge(existMembers, distributedCharge);

    if (existMembers.size() == matchingRoom.getCapacity() - 1) {
      this.autoMatchingProducer.sendMatchRoomCompletedEvent(
          MatchRoomCompletedEvent.of(matchingRoom.getId())
      );
    }
  }

  private void updateExistMembersCharge(List<MemberMatchingRoomChargingInfo> existMembers, int charge) {
    for (MemberMatchingRoomChargingInfo memberMatchingRoomChargingInfo : existMembers) {
      memberMatchingRoomChargingInfo.setCharge(charge);
    }
    this.memberMatchingRoomChargingInfoRepository.saveAll(existMembers);
  }

  public void leaveMemberFromMatchingRoom(MatchMemberCancelledEvent matchMemberCancelledEvent) {
    Members members = this.memberService.findById(matchMemberCancelledEvent.memberId());
    MatchingRoom matchingRoom = this.matchingRoomRepository.findById(
        matchMemberCancelledEvent.roomId()).orElseThrow(
        NoSuchMatchingRoomException::new);

    MemberMatchingRoomChargingInfo memberMatchingRoomChargingInfo = this.memberMatchingRoomChargingInfoRepository.findByMembersAndMatchingRoom(
            members, matchingRoom)
        .orElseThrow(MemberNotInMatchingRoomException::new);

    memberMatchingRoomChargingInfo.leftMatchingRoom();

    this.memberMatchingRoomChargingInfoRepository.save(memberMatchingRoomChargingInfo);

    if (members.isRoomMaster(matchingRoom)) {
      this.findNextRoomMaster(matchingRoom, members)
          .ifPresentOrElse(
              nextRoomMaster -> matchingRoom.changeRoomMaster(nextRoomMaster),
              () -> this.autoMatchingProducer.sendMatchRoomCancelledEvent(
                  MatchRoomCancelledEvent.of(matchingRoom.getId())
              )
          );
    }
  }

  private Optional<Members> findNextRoomMaster(MatchingRoom matchingRoom, Members members) {
    List<MemberMatchingRoomChargingInfo> existMembers = this.memberMatchingRoomChargingInfoRepository.findByMatchingRoom(
        matchingRoom);

    return existMembers.stream()
        .map(MemberMatchingRoomChargingInfo::getMembers)
        .filter(member -> !member.equals(members))
        .findFirst();
  }

  public void cancelMatchingRoom(MatchRoomCancelledEvent matchRoomCancelledEvent) {
    MatchingRoom matchingRoom = getMatchingRoomById(matchRoomCancelledEvent.roomId());

    matchingRoom.cancelMatchingRoom();
    this.matchingRoomRepository.save(matchingRoom);
  }

  private MatchingRoom getMatchingRoomById(Long roomId) {
    return this.matchingRoomRepository.findById(roomId).orElseThrow(NoSuchMatchingRoomException::new);
  }

  public void completeMatchingRoom(MatchRoomCompletedEvent matchRoomCompletedEvent) {
    MatchingRoom matchingRoom = this.getMatchingRoomById(matchRoomCompletedEvent.roomId());

    matchingRoom.completeMatchingRoom();
    this.matchingRoomRepository.save(matchingRoom);
  }
}
