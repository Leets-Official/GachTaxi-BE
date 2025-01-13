package com.gachtaxi.global.config.kafka;

import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchMemberJoinedEvent;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.MatchRoomCreatedEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  @Qualifier("matchRoomCreatedEventProducerFactory")
  public ProducerFactory<String, MatchRoomCreatedEvent> matchRoomCreatedEventProducerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    configs.put(ProducerConfig.ACKS_CONFIG, "all");
    configs.put(ProducerConfig.RETRIES_CONFIG, 3);

    return new DefaultKafkaProducerFactory<>(configs);
  }

  @Bean
  @Qualifier("matchMemberJoinedEventProducerFactory")
  public ProducerFactory<String, MatchMemberJoinedEvent> matchMemberJoinedEventProducerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    configs.put(ProducerConfig.ACKS_CONFIG, "all");
    configs.put(ProducerConfig.RETRIES_CONFIG, 3);

    return new DefaultKafkaProducerFactory<>(configs);
  }

  @Bean
  @Qualifier("matchMemberJoinedEventKafkaTemplate")
  public KafkaTemplate<String, MatchMemberJoinedEvent> matchMemberJoinedEventKafkaTemplate() {
    return new KafkaTemplate<>(matchMemberJoinedEventProducerFactory());
  }

  @Bean
  @Qualifier("matchRoomCreatedEventKafkaTemplate")
  public KafkaTemplate<String, MatchRoomCreatedEvent> matchRoomCreatedEventKafkaTemplate() {
    return new KafkaTemplate<>(matchRoomCreatedEventProducerFactory());
  }
}