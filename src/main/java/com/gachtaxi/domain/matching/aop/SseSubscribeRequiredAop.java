package com.gachtaxi.domain.matching.aop;

import com.gachtaxi.domain.matching.common.controller.ResponseMessage;
import com.gachtaxi.domain.matching.common.exception.ControllerNotHasCurrentMemberIdException;
import com.gachtaxi.domain.matching.common.service.AutoMatchingService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import java.lang.reflect.Parameter;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    Parameter[] parameters = signature.getMethod().getParameters();

    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      if (parameter.getType().equals(Long.class) && parameter.isAnnotationPresent(
          CurrentMemberId.class)) {
        memberId = (Long) proceedingJoinPoint.getArgs()[i];
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
