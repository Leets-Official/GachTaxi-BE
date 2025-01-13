package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.chat.exception.ChattingParticipantNotFoundException;
import com.gachtaxi.domain.chat.repository.ChattingParticipantRepository;
import com.gachtaxi.domain.members.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingParticipantService {

    private final ChattingParticipantRepository chattingParticipantRepository;

    public void save(ChattingParticipant chattingParticipant) {
        chattingParticipantRepository.save(chattingParticipant);
    }

    public ChattingParticipant find(ChattingRoom chattingRoom, Members member) {
        return chattingParticipantRepository.findByChattingRoomAndMembers(chattingRoom, member)
                .orElseThrow(ChattingParticipantNotFoundException::new);
    }

    public void delete(ChattingParticipant chattingParticipant) {
        chattingParticipantRepository.delete(chattingParticipant);
    }
}
