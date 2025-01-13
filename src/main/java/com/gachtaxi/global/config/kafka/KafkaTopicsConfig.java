package com.gachtaxi.global.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicsConfig {

  @Value("${gachtaxi.kafka.topics.match-room-created}")
  private String matchRoomCreatedTopic;

  @Value("${gachtaxi.kafka.topics.match-member-joined}")
  private String matchMemberJoinedTopic;

  @Value("${gachtaxi.kafka.topics.match-room-cancelled}")
  private String matchRoomCancelledTopic;

  @Value("${gachtaxi.kafka.topics.match-room-completed}")
  private String matchRoomCompletedTopic;

  @Value("${gachtaxi.kafka.topics.match-member-cancelled}")
  private String matchMemberCancelledTopic;

  @Value("${gachtaxi.kafka.partition-count}")
  private short partitionCount;

  @Value("${gachtaxi.kafka.replication-factor}")
  private short replicationFactor;

  @Bean
  public NewTopic matchRoomCreatedTopic() {
    return new NewTopic(matchRoomCreatedTopic, partitionCount, replicationFactor);
  }

  @Bean
  public NewTopic matchMemberJoinedTopic() {
    return new NewTopic(matchMemberJoinedTopic, partitionCount, replicationFactor);
  }

  @Bean
  public NewTopic matchRoomCancelledTopic() {
    return new NewTopic(matchRoomCancelledTopic, partitionCount, replicationFactor);
  }

  @Bean
  public NewTopic matchMemberCancelledTopic() {
    return new NewTopic(matchMemberCancelledTopic, partitionCount, replicationFactor);
  }

  @Bean
  public NewTopic matchRoomCompletedTopic() {
    return new NewTopic(matchRoomCompletedTopic, partitionCount, replicationFactor);
  }
}