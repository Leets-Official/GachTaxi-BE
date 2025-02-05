package com.gachtaxi.domain.chat.repository;

import com.gachtaxi.domain.chat.entity.ChattingMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;

public interface ChattingMessageRepository extends MongoRepository<ChattingMessage, String> {
    Slice<ChattingMessage> findAllByRoomIdAndTimeStampAfterOrderByTimeStampDesc(Long roomId, LocalDateTime joinedAt, Pageable pageable);

    @Query("""
            {
                'roomId': ?0,
                'timeStamp': {
                    $gt: ?1,
                    $lt: ?2
                }
            }
            """)
    Slice<ChattingMessage> findAllByRoomIdAndTimeStampAfterAndTimeStampBeforeOrderByTimeStampDesc(Long roomId, LocalDateTime joinedAt, LocalDateTime lastMessageTimeStamp, Pageable pageable);

    Integer countAllByRoomIdAndTimeStampAfterOrderByTimeStampDesc(Long roomId, LocalDateTime timestamp);
}
