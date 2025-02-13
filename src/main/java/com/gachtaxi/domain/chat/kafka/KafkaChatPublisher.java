package com.gachtaxi.domain.chat.kafka;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaChatPublisher {

    private final KafkaTemplate<String, ChatMessage> chatKafkaTemplate;

    @Value("${gachtaxi.kafka.topics.chat-room}")
    private String topic;

    public void publish(ChatMessage chatMessage) {
        log.info("📤 Kafka 채팅 메시지 발행: {}", chatMessage);
        chatKafkaTemplate.send(topic, chatMessage.roomId().toString(), chatMessage);
    }
}
