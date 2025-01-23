package com.gachtaxi.domain.matching.event.service.kafka;

import static com.gachtaxi.global.auth.jwt.util.kafka.KafkaBeanSuffix.KAFKA_TEMPLATE_SUFFIX;

import com.gachtaxi.domain.matching.common.exception.NotDefinedKafkaTemplateException;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchingEvent;
import com.gachtaxi.global.auth.jwt.util.KafkaBeanUtils;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoMatchingProducer {

  private final ApplicationContext applicationContext;

  public void sendEvent(MatchingEvent matchingEvent) {
    String topic = matchingEvent.getTopic();
    Object key = matchingEvent.getKey();

    try {
      KafkaTemplate kafkaTemplate = this.applicationContext.getBean(
          KafkaBeanUtils.getBeanName(topic, KAFKA_TEMPLATE_SUFFIX), KafkaTemplate.class);
      CompletableFuture<?> future = kafkaTemplate.send(matchingEvent.getTopic(), matchingEvent.getKey(), matchingEvent);

      future.thenAccept(result -> {
            if (result instanceof RecordMetadata metadata) {
              log.info("[KAFKA PRODUCER] Success sending MatchRoomCreatedEvent: "
                      + "topic={}, partition={}, offset={}, key={}",
                  metadata.topic(), metadata.partition(), metadata.offset(), key
              );
            }
          }
      ).exceptionally(ex -> {
        log.error("[KAFKA PRODUCER] Failed to send MatchRoomCreatedEvent key={}", key, ex);
        return null;
      });
    } catch (BeansException beansException) {
      throw new NotDefinedKafkaTemplateException();
    }
  }
}
