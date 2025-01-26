package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.exception.ChattingRoomNotFoundException;
import com.gachtaxi.domain.chat.exception.UnSubscriptionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingRedisService {
    public static final String REDIS_CHAT_KEY_PREFIX = "ROOM:";

    private final RedisTemplate<String, Object> chatRoomRedisTemplate;

    public void saveSubscribeMember(long roomId, long senderId) {
        String key = getKey(roomId);

        chatRoomRedisTemplate.opsForSet().add(key, senderId);
    }

    public boolean isActive(long roomId, long senderId) {
        String key = getKey(roomId);

        return Boolean.TRUE.equals(chatRoomRedisTemplate.opsForSet().isMember(key, senderId));
    }

    public void removeSubscribeMember(long roomId, long senderId) {
        String key = getKey(roomId);

        chatRoomRedisTemplate.opsForSet().remove(key, senderId);
    }

    public void checkSubscriptionStatus(long roomId, long senderId) {
        if (!isActive(roomId, senderId)) {
            throw new UnSubscriptionException();
        }
    }

    public long getSubscriberCount(long roomId) {
        String key = getKey(roomId);

        Long size = chatRoomRedisTemplate.opsForSet().size(key);

        if (size == null) {
            throw new ChattingRoomNotFoundException();
        }

        return size;
    }

    private String getKey(long roomId) {
        return REDIS_CHAT_KEY_PREFIX + roomId;
    }
}
