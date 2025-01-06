package com.gachtaxi.domain.matching.common.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.algorithm.service.MatchingAlgorithmService;
import com.gachtaxi.domain.matching.common.dto.enums.AutoMatchingStatus;
import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingPostRequest;
import com.gachtaxi.domain.matching.common.dto.response.AutoMatchingPostResponse;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import com.gachtaxi.domain.matching.event.service.kafka.AutoMatchingProducer;
import com.gachtaxi.domain.matching.event.service.sse.SseService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class AutoMatchingService {

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
        this.matchingAlgorithmService.findRoom(memberId, criteria);

    optionalRoom
        .ifPresentOrElse(
            roomResult -> this.sendMatchMemberJoinedEvent(memberId, roomResult),
            () -> this.sendMatchRoomCreatedEvent(memberId, autoMatchingPostRequest)
        );

    return AutoMatchingPostResponse.of(AutoMatchingStatus.REQUESTED);
  }

  private void sendMatchRoomCreatedEvent(Long memberId,
      AutoMatchingPostRequest autoMatchingPostRequest) {
    Long newRoomId = this.matchingAlgorithmService.createRoom(memberId,
        autoMatchingPostRequest.getCriteria());

    MatchRoomCreatedEvent createdEvent = MatchRoomCreatedEvent.builder()
        .roomId(newRoomId)
        .hostMemberId(memberId)
        .createdAt(LocalDateTime.now())
        .build();

    this.autoMatchingProducer.sendMatchRoomCreatedEvent(createdEvent);
  }

  private void sendMatchMemberJoinedEvent(Long memberId, FindRoomResult roomResult) {
    Long roomId = roomResult.roomId();

    this.matchingAlgorithmService.joinRoom(roomId, memberId);

    MatchMemberJoinedEvent joinedEvent = MatchMemberJoinedEvent.builder()
        .roomId(roomId)
        .memberId(memberId)
        .joinedAt(LocalDateTime.now())
        .build();

    this.autoMatchingProducer.sendMatchMemberJoinedEvent(joinedEvent);
  }
}
