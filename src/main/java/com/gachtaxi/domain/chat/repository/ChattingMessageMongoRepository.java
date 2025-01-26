package com.gachtaxi.domain.chat.repository;

import com.gachtaxi.domain.chat.entity.ChattingMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChattingMessageMongoRepository {

    private static final String COLLECTION = "chatting_messages";
    private final MongoTemplate mongoTemplate;

    public Pair<String, String> updateUnreadCount(Long roomId, LocalDateTime lastReadAt, Long senderId) {
        Query query = new Query().addCriteria(buildCommonCriteria(roomId, lastReadAt, senderId));

        Update update = new Update().inc("unreadCount", -1);

        Pair<String, String> range = getUpdatedMessageRange(roomId, lastReadAt, senderId);
        mongoTemplate.updateMulti(query, update, ChattingMessage.class);

        return range;
    }

    private Pair<String, String> getUpdatedMessageRange(Long roomId, LocalDateTime lastReadAt, Long senderId) {
        Query minQuery = new Query().addCriteria(buildCommonCriteria(roomId, lastReadAt, senderId))
                .with(Sort.by(Sort.Direction.ASC, "_id")).limit(1);

        Query maxQuery = new Query().addCriteria(buildCommonCriteria(roomId, lastReadAt, senderId))
                .with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);

        ChattingMessage minMessage = mongoTemplate.findOne(minQuery, ChattingMessage.class, COLLECTION);
        ChattingMessage maxMessage = mongoTemplate.findOne(maxQuery, ChattingMessage.class, COLLECTION);

        String minId = (minMessage != null) ? minMessage.getId() : "NO_MESSAGES";
        String maxId = (maxMessage != null) ? maxMessage.getId() : "NO_MESSAGES";

        return Pair.of(minId, maxId);
    }

    private Criteria buildCommonCriteria(Long roomId, LocalDateTime lastReadAt, Long senderId) {
        return Criteria.where("roomId").is(roomId)
                .and("timeStamp").gt(lastReadAt)
                .and("senderId").ne(senderId)
                .and("unreadCount").gt(0);
    }
}
