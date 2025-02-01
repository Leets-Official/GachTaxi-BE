package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.response.BlacklistGetResponse;
import com.gachtaxi.domain.members.dto.response.BlacklistPostResponse;
import com.gachtaxi.domain.members.entity.Blacklists;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.BlacklistAlreadyExistsException;
import com.gachtaxi.domain.members.exception.BlacklistNotFoundException;
import com.gachtaxi.domain.members.repository.BlacklistsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistService {

  private static final int PAGE_SIZE = 10;

  private final BlacklistsRepository blacklistsRepository;
  private final MemberService memberService;

  public BlacklistPostResponse save(Long requesterId, Long receiverId) {
    Members requester = this.memberService.findById(requesterId);
    Members receiver = this.memberService.findById(receiverId);

    if (this.blacklistsRepository.existsByRequesterAndReceiver(requester, receiver)) {
      throw new BlacklistAlreadyExistsException();
    }

    Blacklists blacklists = this.blacklistsRepository.save(Blacklists.create(requester, receiver));
    return BlacklistPostResponse.of(blacklists);
  }

  public void delete(Long requesterId, Long receiverId) {
    Members requester = this.memberService.findById(requesterId);
    Members receiver = this.memberService.findById(receiverId);

    Blacklists blacklists = this.blacklistsRepository.findByRequesterAndReceiver(requester,
            receiver)
        .orElseThrow(BlacklistNotFoundException::new);

    this.blacklistsRepository.delete(blacklists);
  }

  public BlacklistGetResponse findBlacklistPage(Long requesterId, int pageNum) {
    Pageable pageRequest = PageRequest.of(pageNum, PAGE_SIZE, Sort.by(Direction.ASC, "receiver.nickname"));

    Page<Blacklists> blacklistsPage = this.blacklistsRepository.findAll(pageRequest);

    return BlacklistGetResponse.of(blacklistsPage);
  }
}
