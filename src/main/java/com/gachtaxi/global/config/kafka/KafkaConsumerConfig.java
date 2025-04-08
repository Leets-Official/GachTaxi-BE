package com.gachtaxi.global.config.kafka;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import com.gachtaxi.domain.matching.event.dto.kafka_topic.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;
  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;
  @Value("${spring.kafka.consumer.chat-group-id}")
  private String chatGroupId;

  // MatchRoomCreatedEvent
  @Bean
  public ConsumerFactory<String, MatchRoomCreatedEvent> matchRoomCreatedEventConsumerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    JsonDeserializer<MatchRoomCreatedEvent> jsonDeserializer =
        new JsonDeserializer<>(MatchRoomCreatedEvent.class);
    jsonDeserializer.addTrustedPackages("com.gachtaxi.domain.matching.event.dto");

    return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, MatchRoomCreatedEvent> matchRoomCreatedEventListenerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, MatchRoomCreatedEvent> factory
        = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(matchRoomCreatedEventConsumerFactory());
    factory.setConcurrency(3);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }

  // MatchMemberJoinedEvent
  @Bean
  public ConsumerFactory<String, MatchMemberJoinedEvent> matchMemberJoinedEventConsumerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    JsonDeserializer<MatchMemberJoinedEvent> jsonDeserializer =
        new JsonDeserializer<>(MatchMemberJoinedEvent.class);
    jsonDeserializer.addTrustedPackages("com.gachtaxi.domain.matching.event.dto");

    return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, MatchMemberJoinedEvent> matchMemberJoinedEventListenerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, MatchMemberJoinedEvent> factory
        = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(matchMemberJoinedEventConsumerFactory());
    factory.setConcurrency(3);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }

  // MatchMemberCanceledEvent
  @Bean
  public ConsumerFactory<String, MatchMemberCancelledEvent> matchMemberCancelledEventConsumerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    JsonDeserializer<MatchMemberCancelledEvent> jsonDeserializer =
        new JsonDeserializer<>(MatchMemberCancelledEvent.class);
    jsonDeserializer.addTrustedPackages("com.gachtaxi.domain.matching.event.dto");

    return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, MatchMemberCancelledEvent> matchMemberCancelledEventListenerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, MatchMemberCancelledEvent> factory
        = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(matchMemberCancelledEventConsumerFactory());
    factory.setConcurrency(3);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }

  // MatchRoomCancelledEvent
  @Bean
  public ConsumerFactory<String, MatchRoomCancelledEvent> matchRoomCancelledEventConsumerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    JsonDeserializer<MatchRoomCancelledEvent> jsonDeserializer =
        new JsonDeserializer<>(MatchRoomCancelledEvent.class);
    jsonDeserializer.addTrustedPackages("com.gachtaxi.domain.matching.event.dto");

    return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, MatchRoomCancelledEvent> matchRoomCancelledEventListenerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, MatchRoomCancelledEvent> factory
        = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(matchRoomCancelledEventConsumerFactory());
    factory.setConcurrency(3);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }

  // MatchRoomCompleted
  @Bean
  public ConsumerFactory<String, MatchRoomCompletedEvent> matchRoomCompletedEventConsumerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    JsonDeserializer<MatchRoomCompletedEvent> jsonDeserializer =
        new JsonDeserializer<>(MatchRoomCompletedEvent.class);
    jsonDeserializer.addTrustedPackages("com.gachtaxi.domain.matching.event.dto");

    return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, MatchRoomCompletedEvent> matchRoomCompletedEventListenerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, MatchRoomCompletedEvent> factory
        = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(matchRoomCompletedEventConsumerFactory());
    factory.setConcurrency(3);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }

  // 채팅
  @Bean
  public ConsumerFactory<String, ChatMessage> chatMessageConsumerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, chatGroupId);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    JsonDeserializer<ChatMessage> jsonDeserializer =
            new JsonDeserializer<>(ChatMessage.class);
    jsonDeserializer.addTrustedPackages("com.gachtaxi.domain.matching.chat.dto");

    return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ChatMessage> chatMessageListenerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, ChatMessage> factory
            = new ConcurrentKafkaListenerContainerFactory<>();

    factory.setConsumerFactory(chatMessageConsumerFactory());
    factory.setConcurrency(3);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }
}