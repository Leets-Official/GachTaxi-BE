package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.MemberNotFoundException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDeleteService {

    private final MemberRepository memberRepository;

    /*
    todo 채팅 메시지 수정 PR 머지 되면 (알수없음)으로 바꾸고 프사 삭제하기
     */
    @Transactional
    public void softDelete(Long memberId) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        member.delete();
    }
}
