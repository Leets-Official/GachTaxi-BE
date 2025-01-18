package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.exception.UnSubscriptionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRedisService {
    public static final String REDIS_CHAT_KEY_PREFIX = "ROOM:";

    private final RedisTemplate<String, Object> chatRoomRedisTemplate;

    public void saveSubscribeMember(long roomId, long senderId) {
        String key = REDIS_CHAT_KEY_PREFIX + roomId;

        chatRoomRedisTemplate.opsForSet().add(key, senderId);
    }

    public boolean isActive(long roomId, long senderId) {
        String key = REDIS_CHAT_KEY_PREFIX + roomId;

        return Boolean.TRUE.equals(chatRoomRedisTemplate.opsForSet().isMember(key, senderId));
    }

    public void removeSubscribeMember(long roomId, long senderId) {
        String key = REDIS_CHAT_KEY_PREFIX + roomId;

        chatRoomRedisTemplate.opsForSet().remove(key, senderId);
    }

    public void checkSubscriptionStatus(long roomId, long senderId) {
        if (!isActive(roomId, senderId)) {
            throw new UnSubscriptionException();
        }
    }
}
