package com.gachtaxi.domain.matching.aop;

import com.gachtaxi.domain.matching.common.controller.ResponseMessage;
import com.gachtaxi.domain.matching.common.exception.ControllerNotHasCurrentMemberIdException;
import com.gachtaxi.domain.matching.common.service.AutoMatchingService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SseSubscribeRequiredAop {

  private final AutoMatchingService autoMatchingService;

  @Around("@annotation(com.gachtaxi.domain.matching.aop.SseSubscribeRequired)")
  public Object checkSseSubscribe(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
    Long memberId = null;
    for (Object arg : proceedingJoinPoint.getArgs()) {
      Class<?> argClass = arg.getClass();
      if (arg instanceof Long && argClass.isAnnotationPresent(CurrentMemberId.class)) {
        memberId = (Long) arg;
        break;
      }
    }
    if (memberId == null) {
      throw new ControllerNotHasCurrentMemberIdException();
    }

    if (!this.autoMatchingService.isSseSubscribed(memberId)) {
      return ApiResponse.response(
          HttpStatus.BAD_REQUEST,
          ResponseMessage.NOT_SUBSCRIBED_SSE.getMessage()
      );
    }

    return proceedingJoinPoint.proceed();
  }
}
