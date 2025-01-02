package com.gachtaxi.domain.chat.redis;

import com.gachtaxi.domain.chat.exception.CustomMessagingException;
import com.gachtaxi.domain.chat.exception.CustomSerializationException;
import com.gachtaxi.domain.chat.exception.RedisSubscribeException;
import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisChatSubscriber implements MessageListener {

    private final RedisTemplate<String, ChatMessage> chatRedisTemplate;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatMessage chatMessage = (ChatMessage) chatRedisTemplate.getValueSerializer().deserialize(message.getBody());

            simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.roomId(), chatMessage);
        } catch (MessagingException e) {
            throw new CustomMessagingException(e.getMessage());
        } catch (SerializationException e) {
            throw new CustomSerializationException(e.getMessage());
        } catch (Exception e) {
            throw new RedisSubscribeException();
        }
    }
}
