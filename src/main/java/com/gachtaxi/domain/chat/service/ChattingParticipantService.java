package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import com.gachtaxi.domain.chat.dto.response.ReadMessageRange;
import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.chat.entity.enums.MessageType;
import com.gachtaxi.domain.chat.exception.ChattingParticipantNotFoundException;
import com.gachtaxi.domain.chat.exception.ChattingRoomNotFoundException;
import com.gachtaxi.domain.chat.exception.DuplicateSubscribeException;
import com.gachtaxi.domain.chat.redis.RedisChatPublisher;
import com.gachtaxi.domain.chat.repository.ChattingMessageMongoRepository;
import com.gachtaxi.domain.chat.repository.ChattingParticipantRepository;
import com.gachtaxi.domain.chat.repository.ChattingRoomRepository;
import com.gachtaxi.domain.members.entity.Members;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChattingParticipantService {

    private final ChattingParticipantRepository chattingParticipantRepository;
    private final ChattingMessageMongoRepository chattingMessageMongoRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingRedisService chattingRedisService;
    private final RedisChatPublisher redisChatPublisher;
    private final ChattingRoomService chattingRoomService;

    @Value("${chat.topic}")
    public String chatTopic;

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

//            checkDuplicateSubscription(chattingRoom.getId(), members.getId());

            Pair<String, String> pair = chattingMessageMongoRepository.updateUnreadCount(chattingRoom.getId(), chattingParticipant.getLastReadAt(), members.getId());

            reEnterEvent(chattingRoom.getId(), members.getId(), members.getNickname(), ReadMessageRange.from(pair));

            chattingParticipant.reSubscribe();

            return true;
        }

        return false;
    }

    public void delete(ChattingParticipant chattingParticipant) {
        chattingParticipantRepository.delete(chattingParticipant);
    }

    public long getParticipantCount(Long roomId) {
        return chattingParticipantRepository.countByChattingRoomId(roomId);
    }

    private void checkDuplicateSubscription(long roomId, long memberId) {
        if (chattingRedisService.isActive(roomId, memberId)) {
            throw new DuplicateSubscribeException();
        }
    }

    private void reEnterEvent(long roomId, long senderId, String senderName, ReadMessageRange range) {
        ChannelTopic topic = new ChannelTopic(chatTopic + roomId);
        ChatMessage chatMessage = ChatMessage.of(roomId, senderId, senderName, range, MessageType.READ);

        redisChatPublisher.publish(topic, chatMessage);
    }

    @Transactional
    public void joinChattingRoom(Members user, Long chattingRoomId) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(ChattingRoomNotFoundException::new);

        if (chattingParticipantRepository.findByChattingRoomAndMembers(chattingRoom, user).isPresent()) {
            return;
        }

        ChattingParticipant chattingParticipant = ChattingParticipant.of(chattingRoom, user);
        chattingParticipantRepository.save(chattingParticipant);
    }
}
