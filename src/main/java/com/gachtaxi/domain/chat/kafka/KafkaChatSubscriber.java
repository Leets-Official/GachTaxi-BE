package com.gachtaxi.domain.chat.kafka;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import com.gachtaxi.domain.chat.exception.CustomMessagingException;
import com.gachtaxi.domain.chat.exception.CustomSerializationException;
import com.gachtaxi.domain.chat.exception.RedisSubscribeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import static com.gachtaxi.domain.chat.redis.RedisChatSubscriber.CHAT_ROOM_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaChatSubscriber {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @KafkaListener(topics = "${gachtaxi.kafka.topics.chat-room}", groupId = "${spring.kafka.consumer.chat-group-id}", containerFactory = "chatMessageListenerFactory")
    public void consumeChatMessage(@Payload ChatMessage chatMessage, Acknowledgment acknowledgment) {
        try {
            log.info("📤 Kafka 채팅 메시지 읽기 성공: {}", chatMessage);

            simpMessageSendingOperations.convertAndSend(CHAT_ROOM_PREFIX + chatMessage.roomId(), chatMessage);
            acknowledgment.acknowledge();
        } catch (MessagingException e) {
            throw new CustomMessagingException(e.getMessage());
        } catch (SerializationException e) {
            throw new CustomSerializationException(e.getMessage());
        } catch (Exception e) {
            throw new RedisSubscribeException(e.getMessage());
        }
    }
}
