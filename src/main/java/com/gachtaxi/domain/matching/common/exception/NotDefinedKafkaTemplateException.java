package com.gachtaxi.domain.matching.common.exception;

import static com.gachtaxi.domain.matching.common.exception.ErrorMessage.NOT_DEFINED_KAFKA_TEMPLATE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.gachtaxi.global.common.exception.BaseException;

public class NotDefinedKafkaTemplateException extends BaseException {

  public NotDefinedKafkaTemplateException() {
    super(INTERNAL_SERVER_ERROR, NOT_DEFINED_KAFKA_TEMPLATE.getMessage());
  }
}
