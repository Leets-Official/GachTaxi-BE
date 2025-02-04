package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.members.dto.response.BlacklistGetResponse;
import com.gachtaxi.domain.members.dto.response.BlacklistPostResponse;
import com.gachtaxi.domain.members.entity.Blacklists;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.BlacklistAlreadyExistsException;
import com.gachtaxi.domain.members.exception.BlacklistNotFoundException;
import com.gachtaxi.domain.members.exception.BlacklistRequesterEqualsReceiverException;
import com.gachtaxi.domain.members.repository.BlacklistsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistService {

  private final BlacklistsRepository blacklistsRepository;
  private final MemberService memberService;

  @Transactional
  public BlacklistPostResponse save(Long requesterId, Long receiverId) {
    Members requester = this.memberService.findById(requesterId);
    Members receiver = this.memberService.findById(receiverId);

    this.checkRequesterAndReceiver(requester, receiver);

    if (this.blacklistsRepository.existsByRequesterAndReceiver(requester, receiver)) {
      throw new BlacklistAlreadyExistsException();
    }

    Blacklists blacklists = this.blacklistsRepository.save(Blacklists.create(requester, receiver));
    return BlacklistPostResponse.of(blacklists);
  }

  @Transactional
  public void delete(Long requesterId, Long receiverId) {
    Members requester = this.memberService.findById(requesterId);
    Members receiver = this.memberService.findById(receiverId);

    this.checkRequesterAndReceiver(requester, receiver);

    Blacklists blacklists = this.blacklistsRepository.findByRequesterAndReceiver(requester,
            receiver)
        .orElseThrow(BlacklistNotFoundException::new);

    this.blacklistsRepository.delete(blacklists);
  }

  public BlacklistGetResponse findBlacklistPage(Long requesterId, int pageNum, int pageSize) {
    Members requester = this.memberService.findById(requesterId);

    Pageable pageRequest = PageRequest.of(pageNum, pageSize, Sort.by(Direction.ASC, "receiver.nickname"));

    Slice<Blacklists> blacklistsPage = this.blacklistsRepository.findAllByRequester(requester, pageRequest);

    return BlacklistGetResponse.of(blacklistsPage);
  }

  public boolean isBlacklistInMatchingRoom(Members requester, MatchingRoom matchingRoom) {
    boolean existBlacklist = matchingRoom.getMemberMatchingRoomChargingInfo().stream()
        .anyMatch(memberMatchingRoomChargingInfo -> this.blacklistsRepository.existsByRequesterAndReceiver(
                requester, memberMatchingRoomChargingInfo.getMembers()));

    return existBlacklist;
  }

  private void checkRequesterAndReceiver(Members requester, Members receiver) {
    if (requester.equals(receiver)) {
      throw new BlacklistRequesterEqualsReceiverException();
    }
  }

  public boolean isUserBlacklistedInRoom(Members requester, MatchingRoom matchingRoom) {
    return matchingRoom.getMemberMatchingRoomChargingInfo().stream()
            .anyMatch(info -> this.blacklistsRepository.existsByRequesterAndReceiver(requester, info.getMembers()));
  }
}
