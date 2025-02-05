package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.exception.UnSubscriptionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChattingRedisService {
    public static final String REDIS_CHAT_KEY_PREFIX = "ROOM:";

    private final RedisTemplate<String, Object> chatRoomRedisTemplate;

    public void saveSubscribeMember(long roomId, long senderId, String profilePicture) {
        String key = getKey(roomId);

        chatRoomRedisTemplate.opsForHash().put(key, String.valueOf(senderId), profilePicture);
    }

    public boolean isActive(long roomId, long senderId) {
        String key = getKey(roomId);

        return Boolean.TRUE.equals(chatRoomRedisTemplate.opsForHash().hasKey(key, String.valueOf(senderId)));
    }

    public void removeSubscribeMember(long roomId, long senderId) {
        String key = getKey(roomId);

        chatRoomRedisTemplate.opsForHash().delete(key, String.valueOf(senderId));
    }

    public void checkSubscriptionStatus(long roomId, long senderId) {
        if (!isActive(roomId, senderId)) {
            throw new UnSubscriptionException();
        }
    }

    public long getSubscriberCount(long roomId) {
        String key = getKey(roomId);

        return chatRoomRedisTemplate.opsForHash().size(key);
    }

    public String getProfilePicture(long roomId, long senderId) {
        String key = getKey(roomId);

        return Optional.ofNullable(chatRoomRedisTemplate.opsForHash().get(key, String.valueOf(senderId)))
                .map(Object::toString)
                .orElse(null);
    }

    private String getKey(long roomId) {
        return REDIS_CHAT_KEY_PREFIX + roomId;
    }
}
