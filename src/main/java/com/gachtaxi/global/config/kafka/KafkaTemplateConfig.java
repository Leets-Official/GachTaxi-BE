package com.gachtaxi.global.config.kafka;

import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCancelledEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCompletedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaTemplateConfig {

  @Bean
  @Qualifier("matchMemberJoinedEventKafkaTemplate")
  public KafkaTemplate<String, MatchMemberJoinedEvent> matchMemberJoinedEventKafkaTemplate(
      ProducerFactory<String, MatchMemberJoinedEvent> matchMemberJoinedEventProducerFactory
  ) {
    return new KafkaTemplate<>(matchMemberJoinedEventProducerFactory);
  }

  @Bean
  @Qualifier("matchRoomCreatedEventKafkaTemplate")
  public KafkaTemplate<String, MatchRoomCreatedEvent> matchRoomCreatedEventKafkaTemplate(
      ProducerFactory<String, MatchRoomCreatedEvent> matchRoomCreatedEventProducerFactory
  ) {
    return new KafkaTemplate<>(matchRoomCreatedEventProducerFactory);
  }

  @Bean
  @Qualifier("matchMemberCanceledEventKafkaTemplate")
  public KafkaTemplate<String, MatchMemberCancelledEvent> matchMemberCancelledEventKafkaTemplate(
      ProducerFactory<String, MatchMemberCancelledEvent> matchMemberCanceledEventProducerFactory
  ) {
    return new KafkaTemplate<>(matchMemberCanceledEventProducerFactory);
  }

  @Bean
  @Qualifier("matchRoomCancelledEventKafkaTemplate")
  public KafkaTemplate<String, MatchRoomCancelledEvent> matchRoomCancelledEventKafkaTemplate(
      ProducerFactory<String, MatchRoomCancelledEvent> matchRoomCancelledEventProducerFactory
  ) {
    return new KafkaTemplate<>(matchRoomCancelledEventProducerFactory);
  }

  @Bean
  @Qualifier("matchRoomCompletedEventKafkaTemplate")
  public KafkaTemplate<String, MatchRoomCompletedEvent> matchRoomCompletedEventKafkaTemplate(
      ProducerFactory<String, MatchRoomCompletedEvent> matchRoomCompletedEventProducerFactory
  ) {
    return new KafkaTemplate<>(matchRoomCompletedEventProducerFactory);
  }
}
