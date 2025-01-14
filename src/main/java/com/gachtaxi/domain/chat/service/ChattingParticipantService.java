package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.chat.entity.enums.Status;
import com.gachtaxi.domain.chat.exception.ChattingParticipantNotFoundException;
import com.gachtaxi.domain.chat.exception.DuplicateSubscribeException;
import com.gachtaxi.domain.chat.repository.ChattingParticipantRepository;
import com.gachtaxi.domain.members.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public ChattingParticipant find(long roomId, long memberId) {
        return chattingParticipantRepository.findByChattingRoomIdAndMembersId(roomId, memberId)
                .orElseThrow(ChattingParticipantNotFoundException::new);
    }

    public boolean checkSubscription(ChattingRoom chattingRoom, Members members) {
        Optional<ChattingParticipant> optionalParticipant = chattingParticipantRepository.findByChattingRoomAndMembers(chattingRoom, members);

        if (optionalParticipant.isPresent()) {
            ChattingParticipant chattingParticipant = optionalParticipant.get();

            checkDuplicateSubscription(chattingParticipant);
            chattingParticipant.subscribe();

            return true;
        }

        return false;
    }

    public void delete(ChattingParticipant chattingParticipant) {
        chattingParticipantRepository.delete(chattingParticipant);
    }

    private void checkDuplicateSubscription(ChattingParticipant chattingParticipant) {
        if (chattingParticipant.getStatus() == Status.ACTIVE) {
            throw new DuplicateSubscribeException();
        }
    }
}
