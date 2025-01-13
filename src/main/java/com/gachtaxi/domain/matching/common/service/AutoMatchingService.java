package com.gachtaxi.domain.matching.common.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.algorithm.service.MatchingAlgorithmService;
import com.gachtaxi.domain.matching.common.dto.enums.AutoMatchingStatus;
import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingCancelledRequest;
import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingPostRequest;
import com.gachtaxi.domain.matching.common.dto.response.AutoMatchingPostResponse;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import com.gachtaxi.domain.matching.event.service.kafka.AutoMatchingProducer;
import com.gachtaxi.domain.matching.event.service.sse.SseService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoMatchingService {

  @Value("${gachtaxi.matching.auto-matching-max-capacity}")
  private int autoMaxCapacity;

  @Value("${gachtaxi.matching.auto-matcnig-description}")
  private String autoDescription;

  private final SseService sseService;
  private final AutoMatchingProducer autoMatchingProducer;
  private final MatchingAlgorithmService matchingAlgorithmService;

  public SseEmitter handleSubscribe(Long userId) {
    return this.sseService.subscribe(userId);
  }

  public boolean isSseSubscribed(Long memberId) {
    return this.sseService.isSubscribed(memberId);
  }

  public AutoMatchingPostResponse handlerAutoRequestMatching(
      Long memberId,
      AutoMatchingPostRequest autoMatchingPostRequest
  ) {
    List<Tags> criteria = autoMatchingPostRequest.getCriteria();
    Optional<FindRoomResult> optionalRoom =
        this.matchingAlgorithmService.findRoom(memberId, autoMatchingPostRequest.startPoint(),
            autoMatchingPostRequest.destinationPoint(), criteria);

    optionalRoom
        .ifPresentOrElse(
            roomResult -> this.sendMatchMemberJoinedEvent(memberId, roomResult),
            () -> this.sendMatchRoomCreatedEvent(memberId, autoMatchingPostRequest)
        );

    return AutoMatchingPostResponse.of(AutoMatchingStatus.REQUESTED);
  }

  private void sendMatchRoomCreatedEvent(Long memberId,
      AutoMatchingPostRequest autoMatchingPostRequest) {
    this.autoMatchingProducer.sendMatchRoomCreatedEvent(MatchRoomCreatedEvent.of(memberId, autoMatchingPostRequest, autoMaxCapacity, autoDescription));
  }

  private void sendMatchMemberJoinedEvent(Long memberId, FindRoomResult roomResult) {
    Long roomId = roomResult.roomId();

    this.autoMatchingProducer.sendMatchMemberJoinedEvent(MatchMemberJoinedEvent.of(roomId, memberId));
  }

  public AutoMatchingPostResponse handlerAutoCancelMatching(Long memberId,
      AutoMatchingCancelledRequest autoMatchingCancelledRequest) {

    this.autoMatchingProducer.sendMatchMemberLeftEvent(MatchMemberCancelledEvent.of(autoMatchingCancelledRequest.roomId(), memberId));

    return AutoMatchingPostResponse.of(AutoMatchingStatus.CANCELLED);
  }
}
