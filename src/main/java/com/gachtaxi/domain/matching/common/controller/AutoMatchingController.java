package com.gachtaxi.domain.matching.common.controller;

import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingPostRequest;
import com.gachtaxi.domain.matching.common.dto.response.AutoMatchingPostResponse;
import com.gachtaxi.domain.matching.common.service.AutoMatchingService;
import com.gachtaxi.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching/auto")
public class AutoMatchingController {

  private final AutoMatchingService autoMatchingService;

  @GetMapping("/subscribe")
  public ApiResponse<SseEmitter> subscribeSse(@RequestParam Long memberId) {
    // TODO: 인가 로직 완성되면 해당 멤버의 아이디를 가져오도록 변경
//    Long memberId = 1L;
    return ApiResponse.response(
        HttpStatus.OK,
        ResponseMessage.SUBSCRIBE_SUCCESS.getMessage(),
        this.autoMatchingService.handleSubscribe(memberId)
    );
  }

  @PostMapping("/request")
  public ApiResponse<AutoMatchingPostResponse> requestMatching(
      @RequestParam Long memberId,
      @RequestBody AutoMatchingPostRequest autoMatchingPostRequest
  ) {
    // TODO: 인가 로직 완성되면 해당 멤버의 아이디를 가져오도록 변경
//    Long memberId = 1L;
    if (!this.autoMatchingService.isSseSubscribed(memberId)) {
      return ApiResponse.response(
          HttpStatus.BAD_REQUEST,
          ResponseMessage.NOT_SUBSCRIBED_SSE.getMessage()
      );
    }

    return ApiResponse.response(
        HttpStatus.OK,
        ResponseMessage.AUTO_MATCHING_REQUEST_ACCEPTED.getMessage(),
        this.autoMatchingService.handlerAutoRequestMatching(memberId, autoMatchingPostRequest)
    );
  }
}