package com.gachtaxi.domain.matching.event.service.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 사용자별 SSE Emitter 관리
 */
@Service
public class SseService {

  private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

  /**
   * SSE 구독
   */
  public SseEmitter subscribe(Long memberId) {
    SseEmitter emitter = new SseEmitter(10 * 60 * 1000L); // 10분
    emitter.onCompletion(() -> emitterMap.remove(memberId));
    emitter.onTimeout(() -> emitterMap.remove(memberId));
    this.emitterMap.put(memberId, emitter);
    return emitter;
  }

  /**
   * userId가 현재 SSE를 구독 중인지 확인
   */
  public boolean isSubscribed(Long memberId) {
    return this.emitterMap.containsKey(memberId);
  }

  /**
   * 특정 사용자에게 이벤트 전송
   */
  public void sendToClient(Long memberId, String eventName, Object data) {
    SseEmitter emitter = this.emitterMap.get(memberId);
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event()
            .name(eventName)
            .data(data));
      } catch (IOException e) {
        this.emitterMap.remove(memberId);
      }
    }
  }

  /**
   * 모든 사용자에게 브로드캐스트
   */
  public void broadcast(String eventName, Object data) {
    for (Map.Entry<Long, SseEmitter> entry : emitterMap.entrySet()) {
      try {
        entry.getValue().send(SseEmitter.event().name(eventName).data(data));
      } catch (IOException e) {
        this.emitterMap.remove(entry.getKey());
      }
    }
  }
}