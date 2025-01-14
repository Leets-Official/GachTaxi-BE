package com.gachtaxi.global.config.kafka;

import static com.gachtaxi.global.auth.jwt.util.kafka.KafkaBeanSuffix.KAFKA_TEMPLATE_SUFFIX;
import static com.gachtaxi.global.auth.jwt.util.kafka.KafkaBeanSuffix.NEW_TOPIC_SUFFIX;
import static com.gachtaxi.global.auth.jwt.util.kafka.KafkaBeanSuffix.PRODUCER_FACTORY_SUFFIX;

import com.gachtaxi.global.auth.jwt.util.KafkaBeanUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class KafkaBeanRegistrar implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

  private Environment environment;

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    Map<String, String> topics = Binder.get(environment)
        .bind("gachtaxi.kafka.topics", Bindable.mapOf(String.class, String.class))
        .orElse(Collections.emptyMap());

    for (String topic : topics.values()) {
      this.registerProducerFactoryAndKafkaTemplate(topic, registry);
      this.registerNewTopic(topic, registry);
    }
  }

  private void registerKafkaTemplate(String topic, String producerFactoryBeanName, BeanDefinitionRegistry registry) {
    String kafkaTemplateBeanName = KafkaBeanUtils.getBeanName(topic, KAFKA_TEMPLATE_SUFFIX);
    AbstractBeanDefinition kafkaTemplateBeanDefinition =
        BeanDefinitionBuilder.genericBeanDefinition(KafkaTemplate.class)
            .addConstructorArgReference(producerFactoryBeanName)
            .getBeanDefinition();

    registry.registerBeanDefinition(kafkaTemplateBeanName, kafkaTemplateBeanDefinition);
  }

  private void registerNewTopic(String topic, BeanDefinitionRegistry registry) {
    short partitionCount = Short.valueOf(this.environment.getProperty("gachtaxi.kafka.partition-count"));
    short replicationFactor = Short.valueOf(this.environment.getProperty("gachtaxi.kafka.replication-factor"));

    String topicBeanName = KafkaBeanUtils.getBeanName(topic, NEW_TOPIC_SUFFIX);
    AbstractBeanDefinition newTopicBeanDefinition =
        BeanDefinitionBuilder.genericBeanDefinition(NewTopic.class)
        .addConstructorArgValue(topic)
        .addConstructorArgValue(partitionCount)
        .addConstructorArgValue(replicationFactor)
        .getBeanDefinition();

    registry.registerBeanDefinition(topicBeanName, newTopicBeanDefinition);
  }

  private void registerProducerFactoryAndKafkaTemplate(String topic, BeanDefinitionRegistry registry) {
    String producerBeanName = KafkaBeanUtils.getBeanName(topic, PRODUCER_FACTORY_SUFFIX);
    AbstractBeanDefinition producerBeanDefinition =
        BeanDefinitionBuilder.genericBeanDefinition(DefaultKafkaProducerFactory.class)
            .addConstructorArgValue(this.getProducerOptions())
            .getBeanDefinition();

    registry.registerBeanDefinition(producerBeanName, producerBeanDefinition);

    this.registerKafkaTemplate(topic, producerBeanName, registry);
  }

  private Map<String, Object> getProducerOptions() {
    String bootstrapServers = this.environment.getProperty("spring.kafka.bootstrap-servers");

    Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    configs.put(ProducerConfig.ACKS_CONFIG, "all");
    configs.put(ProducerConfig.RETRIES_CONFIG, 3);

    return configs;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}
