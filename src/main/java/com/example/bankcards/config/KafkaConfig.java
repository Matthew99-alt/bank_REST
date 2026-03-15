package com.example.bankcards.config;


import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.producer.KafkaClientProducer;
import com.example.bankcards.dto.CardDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${t1.kafka.consumer.group-id}")
    private String groupId;
    @Value("${t1.kafka.bootstrap.server}")
    private String servers;
    @Value("${t1.kafka.session.timeout.ms:15000}")
    private String sessionTimeout;
    @Value("${t1.kafka.max.partition.fetch.bytes:300000}")
    private String maxPartitionFetchBytes;
    @Value("${t1.kafka.max.poll.records:1}")
    private String maxPollRecords;
    @Value("${t1.kafka.max.poll.interval.ms:3000}")
    private String maxPollIntervalsMs;
    @Value("${t1.kafka.topic.client_id_registered}")
    private String clientTopic;

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Bean
    public ConsumerFactory<String, CardDTO> consumerListenerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // Включаем ErrorHandlingDeserializer для ключа и значения
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // Делегаты для реальной десериализации
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        // Настройки JsonDeserializer
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, CardDTO.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalsMs);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, CardDTO> kafkaListenerContainerFactory(@Qualifier("consumerListenerFactory") ConsumerFactory<String, CardDTO> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CardDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory, ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
    }


    // для повторной попытки почитать сообщения
    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.error(" RetryListeners message = {}, offset = {} deliveryAttempt = {}", ex.getMessage(), record.offset(), deliveryAttempt);
        });
        return handler;
    }

    @Bean("client")
    public KafkaTemplate<String, CardDTO> kafkaTemplate(ProducerFactory<String, CardDTO> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "сom.example.bankcards.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaClientProducer producerClient(@Qualifier("client") KafkaTemplate template) {
        template.setDefaultTopic(clientTopic);
        return new KafkaClientProducer(template);
    }

    @Bean
    public ProducerFactory<String, TransactionDTO> transferProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean("transferKafkaTemplate")
    public KafkaTemplate<String, TransactionDTO> transferKafkaTemplate(
            @Qualifier("transferProducerFactory") ProducerFactory<String, TransactionDTO> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    public ProducerFactory<String, CardDTO> producerClientFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, TransactionDTO> transferConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "transfer-group"); // отдельная группа для переводов
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TransactionDTO.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // можно добавить остальные настройки (таймауты и т.д.) при необходимости

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionDTO> transferKafkaListenerContainerFactory(
            @Qualifier("transferConsumerFactory") ConsumerFactory<String, TransactionDTO> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, TransactionDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(false); // или true, если нужно батчами
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(errorHandler()); // переиспользуем ваш errorHandler
        return factory;
    }
}
