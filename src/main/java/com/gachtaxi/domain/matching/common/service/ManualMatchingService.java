package com.gachtaxi.domain.matching.common.service;

import com.gachtaxi.domain.matching.common.dto.request.ManualMatchingRequest;
import com.gachtaxi.domain.matching.common.dto.response.MatchingRoomResponse;
import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.MemberMatchingRoomChargingInfo;
import com.gachtaxi.domain.matching.common.entity.Route;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomType;
import com.gachtaxi.domain.matching.common.entity.enums.PaymentStatus;
import com.gachtaxi.domain.matching.common.exception.DuplicatedMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.NotEqualStartAndDestinationException;
import com.gachtaxi.domain.matching.common.exception.PageNotFoundException;
import com.gachtaxi.domain.matching.common.exception.RoomMasterCantJoinException;
import com.gachtaxi.domain.matching.common.exception.MemberAlreadyJoinedException;
import com.gachtaxi.domain.matching.common.exception.MemberAlreadyLeftMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.MemberNotInMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.NoSuchMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.NotActiveMatchingRoomException;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.matching.common.repository.MemberMatchingRoomChargingInfoRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.service.MemberService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManualMatchingService {

    private final MemberService memberService;
    private final MatchingRoomService matchingRoomService;
    private final MatchingRoomRepository matchingRoomRepository;
    private final MemberMatchingRoomChargingInfoRepository memberMatchingRoomChargingInfoRepository;

    /*
      수동 매칭 방 생성
    */
    @Transactional
    public Long createManualMatchingRoom(Long userId, ManualMatchingRequest request) {
        Members roomMaster = memberService.findById(userId);

        if (matchingRoomRepository.existsByMemberInMatchingRoom(roomMaster)) {
            throw new DuplicatedMatchingRoomException();
        }

        if (request.departure().equals(request.destination())) {
            throw new NotEqualStartAndDestinationException();
        }

        Route route = matchingRoomService.saveRoute(request.departure(), request.destination());

        MatchingRoom matchingRoom = MatchingRoom.manualOf(
                roomMaster,
                route,
                request.title(),
                request.description(),
                4,
                request.totalCharge(),
                request.departureTime()
        );

        MatchingRoom savedMatchingRoom = matchingRoomRepository.save(matchingRoom);

        matchingRoomService.saveMatchingRoomTagInfoForManual(savedMatchingRoom, request.getCriteria());
        matchingRoomService.saveRoomMasterChargingInfoForManual(savedMatchingRoom, roomMaster);

        return savedMatchingRoom.getId();
    }

    /*
      수동 매칭방 참여
     */
    @Transactional
    public void joinManualMatchingRoom(Long userId, Long roomId) {
        Members user = memberService.findById(userId);

        MatchingRoom matchingRoom = this.matchingRoomRepository.findById(roomId)
                .orElseThrow(NoSuchMatchingRoomException::new);

        if (!matchingRoom.isActive()) {
            throw new NotActiveMatchingRoomException();
        }

        if (user.isRoomMaster(matchingRoom)) {
            throw new RoomMasterCantJoinException();
        }

        if (this.memberMatchingRoomChargingInfoRepository.existsByMembersAndMatchingRoom(user, matchingRoom)) {
            throw new MemberAlreadyJoinedException();
        }

        Optional<MemberMatchingRoomChargingInfo> joinedInPast = this.memberMatchingRoomChargingInfoRepository
                .findByMembersAndMatchingRoom(user, matchingRoom);

        MemberMatchingRoomChargingInfo memberInfo;
        if (joinedInPast.isPresent()) {
            memberInfo = joinedInPast.get().joinMatchingRoom();
        } else {
            memberInfo = MemberMatchingRoomChargingInfo.notPayedOf(matchingRoom, user);
        }

        this.memberMatchingRoomChargingInfoRepository.save(memberInfo);

        List<MemberMatchingRoomChargingInfo> existMembers = this.memberMatchingRoomChargingInfoRepository
                .findByMatchingRoomAndPaymentStatus(matchingRoom, PaymentStatus.NOT_PAYED);

        int distributedCharge = (int) Math.ceil((double) matchingRoom.getTotalCharge() / (existMembers.size() + 1));

        matchingRoomService.updateExistMembersChargeForManual(existMembers, distributedCharge);

        int nowMemberCount = existMembers.size() + 1;

        if (matchingRoom.isFull(nowMemberCount)) {
            matchingRoom.completeMatchingRoom();
            this.matchingRoomRepository.save(matchingRoom);
        }
    }
    /*
      todo 수동 매칭 → 자동 매칭 전환 : 추후 고도화시, 10분전에 유저에게 알림을 주고 자동 매칭으로 전환
    */
    @Transactional
    public void convertToAutoMatching(Long roomId) {
        MatchingRoom matchingRoom = this.matchingRoomRepository.findById(roomId)
                .orElseThrow(NoSuchMatchingRoomException::new);

        if (!matchingRoom.isActive()) {
            throw new NotActiveMatchingRoomException();
        }

        if (LocalDateTime.now().isAfter(matchingRoom.getDepartureTime().minusMinutes(10))) {

            int currentMembers = this.memberMatchingRoomChargingInfoRepository
                    .countByMatchingRoomAndPaymentStatus(matchingRoom, PaymentStatus.NOT_PAYED);

            if (matchingRoom.isAutoConvertible(currentMembers)) {
                matchingRoom.convertToAutoMatching();
                matchingRoomRepository.save(matchingRoom);
            }
        }
    }

    /*
      방장 취소 + 방 삭제
    */
    @Transactional
    public void leaveManualMatchingRoom(Long userId, Long roomId) {
        Members user = this.memberService.findById(userId);
         MatchingRoom matchingRoom = this.matchingRoomRepository.findById(roomId)
                 .orElseThrow(NoSuchMatchingRoomException::new);

         MemberMatchingRoomChargingInfo memberMatchingRoomChargingInfo =
                 this.memberMatchingRoomChargingInfoRepository.findByMembersAndMatchingRoom(user, matchingRoom)
                         .orElseThrow(MemberNotInMatchingRoomException::new);

         if (memberMatchingRoomChargingInfo.isAlreadyLeft()) {
              throw new MemberAlreadyLeftMatchingRoomException();
         }

         memberMatchingRoomChargingInfo.leftMatchingRoom();
         this.memberMatchingRoomChargingInfoRepository.save(memberMatchingRoomChargingInfo);

         if (user.isRoomMaster(matchingRoom)) {
             List<MemberMatchingRoomChargingInfo> remainingMembers =
                     this.memberMatchingRoomChargingInfoRepository.findByMatchingRoomAndPaymentStatus(matchingRoom, PaymentStatus.NOT_PAYED);

             if (remainingMembers.isEmpty()) {
                 matchingRoom.cancelMatchingRoom();
                 this.matchingRoomRepository.save(matchingRoom);
             } else {
                 Members newRoomMaster = remainingMembers.get(0).getMembers();
                 matchingRoom.changeRoomMaster(newRoomMaster);
                 this.matchingRoomRepository.save(matchingRoom);
             }
         }
    }
    /*
       수동 매칭 방 리스트 조회
    */
    @Transactional
    public Page<MatchingRoomResponse> getManualMatchingList(int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new PageNotFoundException();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<MatchingRoom> rooms = matchingRoomRepository.findByMatchingRoomTypeAndMatchingRoomStatus(
                MatchingRoomType.MANUAL, MatchingRoomStatus.ACTIVE, pageable);

        return rooms.map(MatchingRoomResponse::from);
    }
    /*
       나의 매칭방 리스트 조회
     */
    @Transactional
    public Page<MatchingRoomResponse> getMyMatchingList(Long userId, int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new PageNotFoundException();
        }

        Members user = memberService.findById(userId);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<MatchingRoom> rooms = matchingRoomRepository.findByMemberInMatchingRoom(user, pageable);

        return rooms.map(MatchingRoomResponse::from);
    }
}

