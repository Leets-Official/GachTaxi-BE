package com.gachtaxi.domain.chat.event;

import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import com.gachtaxi.domain.chat.exception.ChattingParticipantNotFoundException;
import com.gachtaxi.domain.chat.service.ChattingParticipantService;
import com.gachtaxi.domain.chat.service.ChattingRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import static com.gachtaxi.domain.chat.stomp.strategy.StompConnectStrategy.CHAT_USER_ID;
import static com.gachtaxi.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_ROOM_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventHandler {

    private final ChattingParticipantService chattingParticipantService;
    private final ChattingRedisService chattingRedisService;

    @EventListener
    @Transactional
    public void handleDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        try {
            long roomId = (long) accessor.getSessionAttributes().get(CHAT_ROOM_ID);
            long userId = (long) accessor.getSessionAttributes().get(CHAT_USER_ID);

            ChattingParticipant chattingParticipant = chattingParticipantService.find(roomId, userId);

            if (chattingRedisService.isActive(roomId, userId)) {
                chattingParticipant.disconnect();
                chattingRedisService.removeSubscribeMember(roomId, userId);
            }
        } catch (NullPointerException e) {
            log.info("[handleDisconnect] 구독 정보가 존재하지 않습니다.");
        } catch (ChattingParticipantNotFoundException e) {
            log.warn("[handleDisconnect] 이미 퇴장한 참여자 입니다.");
        }
    }

    @EventListener
    @Transactional
    public void handleUnsubscribe(SessionUnsubscribeEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        long roomId = (long) accessor.getSessionAttributes().get(CHAT_ROOM_ID);
        long userId = (long) accessor.getSessionAttributes().get(CHAT_USER_ID);

        try {
            ChattingParticipant chattingParticipant = chattingParticipantService.find(roomId, userId);

            if (chattingRedisService.isActive(roomId, userId)) {
                chattingParticipant.unsubscribe();
                chattingRedisService.removeSubscribeMember(roomId, userId);
            }
        } catch (ChattingParticipantNotFoundException e) {
            log.warn("[handleUnsubscribe] 이미 퇴장한 참여자 입니다.");
        }
    }
}
