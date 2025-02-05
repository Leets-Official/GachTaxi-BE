package com.gachtaxi.domain.matching.common.service;

import com.gachtaxi.domain.matching.algorithm.dto.FindRoomResult;
import com.gachtaxi.domain.matching.algorithm.service.MatchingAlgorithmService;
import com.gachtaxi.domain.matching.common.dto.enums.AutoMatchingStatus;
import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingCancelledRequest;
import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingPostRequest;
import com.gachtaxi.domain.matching.common.dto.response.AutoMatchingPostResponse;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.matching.event.MatchingEventFactory;
import com.gachtaxi.domain.matching.event.service.kafka.AutoMatchingProducer;
import com.gachtaxi.domain.matching.event.service.sse.SseService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoMatchingService {

  private final SseService sseService;
  private final MatchingAlgorithmService matchingAlgorithmService;
  private final MatchingEventFactory matchingEventFactory;
  private final AutoMatchingProducer autoMatchingProducer;

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
//    String[] startCoordinates = autoMatchingPostRequest.startPoint().split(",");
//    double startLongitude = Double.parseDouble(startCoordinates[0]);
//    double startLatitude = Double.parseDouble(startCoordinates[1]);
//
//    String[] destinationCoordinates = autoMatchingPostRequest.destinationPoint().split(",");
//    double destinationLongitude = Double.parseDouble(destinationCoordinates[0]);
//    double destinationLatitude = Double.parseDouble(destinationCoordinates[1]);

//    Optional<FindRoomResult> optionalRoom =
//        this.matchingAlgorithmService.findRoom(memberId, startLongitude, startLatitude, destinationLongitude, destinationLatitude, criteria);



    Optional<FindRoomResult> optionalRoom =
        this.matchingAlgorithmService.findRoom(
                memberId,
                autoMatchingPostRequest.departure(),
                autoMatchingPostRequest.destination(),
                criteria
        );
//    Optional<FindRoomResult> optionalRoom =
//        this.matchingAlgorithmService.findRoom(memberId, autoMatchingPostRequest.startPoint(), autoMatchingPostRequest.destinationPoint(), criteria);
    optionalRoom
        .ifPresentOrElse(
            roomResult -> this.sendMatchMemberJoinedEvent(memberId, roomResult),
            () -> this.sendMatchRoomCreatedEvent(memberId, autoMatchingPostRequest)
        );

    return AutoMatchingPostResponse.of(AutoMatchingStatus.REQUESTED);
  }

  private void sendMatchRoomCreatedEvent(Long memberId,
      AutoMatchingPostRequest autoMatchingPostRequest) {
    this.autoMatchingProducer.sendEvent(this.matchingEventFactory.createMatchRoomCreatedEvent(memberId, autoMatchingPostRequest));
  }

  private void sendMatchMemberJoinedEvent(Long memberId, FindRoomResult roomResult) {
    Long roomId = roomResult.roomId();
    this.autoMatchingProducer.sendEvent(this.matchingEventFactory.createMatchMemberJoinedEvent(roomId, memberId, roomResult.chattingRoomId()));
  }

  public AutoMatchingPostResponse handlerAutoCancelMatching(Long memberId,
      AutoMatchingCancelledRequest autoMatchingCancelledRequest) {

    this.autoMatchingProducer.sendEvent(this.matchingEventFactory.createMatchMemberCancelledEvent(autoMatchingCancelledRequest.roomId(), memberId));

    return AutoMatchingPostResponse.of(AutoMatchingStatus.CANCELLED);
  }
}
