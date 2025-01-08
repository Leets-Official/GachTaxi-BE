package com.gachtaxi.domain.chat.stomp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Configuration
public class StompErrorHandler extends StompSubProtocolErrorHandler {
}
