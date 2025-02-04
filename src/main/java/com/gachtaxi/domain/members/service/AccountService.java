package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.response.AccountGetResponse;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final MemberService memberService;

  private final MemberRepository memberRepository;

  public AccountGetResponse getAccount(Long memberId) {
    Members members = this.memberService.findById(memberId);

    return AccountGetResponse.of(members);
  }

  @Transactional
  public void updateAccount(Long memberId, String accountNumber) {
    Members member = this.memberService.findById(memberId);

    member.setAccountNumber(accountNumber);
    this.memberRepository.save(member);
  }
}
