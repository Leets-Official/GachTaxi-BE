package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import com.gachtaxi.domain.chat.dto.request.ChattingRoomResponse;
import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.chat.exception.ChattingRoomNotFoundException;
import com.gachtaxi.domain.chat.redis.RedisChatPublisher;
import com.gachtaxi.domain.chat.repository.ChattingParticipantRepository;
import com.gachtaxi.domain.chat.repository.ChattingRoomRepository;
import com.gachtaxi.domain.members.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingParticipantRepository chattingParticipantRepository;
    private final MemberService memberService;
    private final RedisChatPublisher redisChatPublisher;

    @Value("${chat.topic}")
    public String chatTopic;

    @Transactional
    public ChattingRoomResponse save() {
        ChattingRoom chattingRoom = ChattingRoom.builder().build();

        chattingRoomRepository.save(chattingRoom);
        return ChattingRoomResponse.from(chattingRoom);
    }

    @Transactional
    public void delete(long chattingRoomId) {
        ChattingRoom chattingRoom = find(chattingRoomId);

        chattingRoom.delete();
    }

    @Transactional
    public void subscribeChatRoom(long roomId, long senderId, String senderName) {
        ChattingRoom chattingRoom = find(roomId);
//        Members members = memberService.find();

        ChannelTopic topic = new ChannelTopic(chatTopic + roomId);
        ChatMessage chatMessage = ChatMessage.subscribe(roomId, senderId, senderName, senderName+" 님이 입장하셨습니다.");

//        ChattingParticipant chattingParticipant = ChattingParticipant.of(chattingRoom, members);
//        chattingParticipantRepository.save(chattingParticipant);

        redisChatPublisher.publish(topic, chatMessage);
    }

    public ChattingRoom find(long chattingRoomId) {
        return chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(ChattingRoomNotFoundException::new);
    }
}
