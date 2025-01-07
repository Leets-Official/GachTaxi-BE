package com.gachtaxi.domain.chat.repository;

import com.gachtaxi.domain.chat.entity.ChattingMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChattingMessageRepository extends MongoRepository<ChattingMessage, String> {
}
