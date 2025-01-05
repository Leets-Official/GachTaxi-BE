package com.gachtaxi.domain.chat.interceptor.strategy;

import com.gachtaxi.global.auth.jwt.exception.TokenNotExistException;
import com.gachtaxi.global.auth.jwt.util.JwtExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import static com.gachtaxi.global.auth.jwt.util.JwtProvider.ACCESS_TOKEN_SUBJECT;

@Component
@RequiredArgsConstructor
public class StompConnectStrategy implements StompCommandStrategy{

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String USER_ID_KEY = "USER_ID";

    private final JwtExtractor jwtExtractor;

    @Override
    public boolean supports(StompCommand command) {
        return StompCommand.CONNECT.equals(command);
    }

    @Override
    public Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
        String jwtToken = accessor.getFirstNativeHeader(ACCESS_TOKEN_SUBJECT);

        if(jwtToken == null || !jwtToken.startsWith(TOKEN_PREFIX)) {
            throw new TokenNotExistException();
        }

        /*
        todo 인증 객체 생성 후 설정하기
         */

        Long userId = jwtExtractor.getId(jwtToken);
        accessor.getSessionAttributes().put(USER_ID_KEY, userId);

        return message;
    }
}
