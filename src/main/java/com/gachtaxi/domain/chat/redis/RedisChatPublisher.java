package com.gachtaxi.domain.chat.redis;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisChatPublisher {

    private final RedisTemplate<String, ChatMessage> chatRedisTemplate;

    public void publish(ChannelTopic topic, ChatMessage chatMessage) {
        chatRedisTemplate.convertAndSend(topic.getTopic(), chatMessage);
    }
}
