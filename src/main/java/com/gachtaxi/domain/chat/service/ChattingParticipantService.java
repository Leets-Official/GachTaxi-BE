package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import com.gachtaxi.domain.chat.dto.response.ReadMessageRange;
import com.gachtaxi.domain.chat.entity.ChattingMessage;
import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.chat.entity.enums.MessageType;
import com.gachtaxi.domain.chat.exception.ChattingParticipantNotFoundException;
import com.gachtaxi.domain.chat.exception.DuplicateSubscribeException;
import com.gachtaxi.domain.chat.redis.RedisChatPublisher;
import com.gachtaxi.domain.chat.repository.ChattingParticipantRepository;
import com.gachtaxi.domain.members.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChattingParticipantService {

    private final ChattingParticipantRepository chattingParticipantRepository;
    private final ChattingRedisService chattingRedisService;
    private final MongoTemplate mongoTemplate;
    private final RedisChatPublisher redisChatPublisher;

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

            checkDuplicateSubscription(chattingRoom.getId(), members.getId());

            updateUnreadCount(chattingRoom.getId(), chattingParticipant.getLastReadAt(), members.getId());

            Pair<String, String> pair = getUpdatedMessageRange(chattingRoom.getId(), chattingParticipant.getLastReadAt(), members.getId());

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

    public void updateUnreadCount(Long roomId, LocalDateTime lastReadAt, Long senderId) {
        Query query = new Query().addCriteria(
                Criteria.where("roomId").is(roomId)
                        .and("timeStamp").gt(lastReadAt)
                        .and("senderId").ne(senderId)
                        .and("unreadCount").gt(0)
        );

        Update update = new Update().inc("unreadCount", -1);

        mongoTemplate.updateMulti(query, update, ChattingMessage.class);
    }

    public Pair<String, String> getUpdatedMessageRange(Long roomId, LocalDateTime lastReadAt, Long senderId) {
        Query minQuery = new Query().addCriteria(
                Criteria.where("roomId").is(roomId)
                        .and("timeStamp").gt(lastReadAt)
                        .and("senderId").ne(senderId)
                        .and("unreadCount").gt(0)
        ).with(Sort.by(Sort.Direction.ASC, "_id")).limit(1);

        Query maxQuery = new Query().addCriteria(
                Criteria.where("roomId").is(roomId)
                        .and("timeStamp").gt(lastReadAt)
                        .and("senderId").ne(senderId)
                        .and("unreadCount").gt(0)
        ).with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);

        ChattingMessage minMessage = mongoTemplate.findOne(minQuery, ChattingMessage.class, "chatting_messages");
        ChattingMessage maxMessage = mongoTemplate.findOne(maxQuery, ChattingMessage.class, "chatting_messages");

        String minId = (minMessage != null) ? minMessage.getId() : "NO_MESSAGES";
        String maxId = (maxMessage != null) ? maxMessage.getId() : "NO_MESSAGES";

        return Pair.of(minId, maxId);
    }

    private void reEnterEvent(long roomId, long senderId, String senderName, ReadMessageRange range) {
        ChannelTopic topic = new ChannelTopic(chatTopic + roomId);
        ChatMessage chatMessage = ChatMessage.of(roomId, senderId, senderName, range, MessageType.READ);

        redisChatPublisher.publish(topic, chatMessage);
    }
}
