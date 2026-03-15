package com.example.bankcards.consumer;

import com.example.bankcards.dto.CardDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClientConsumer {
    private final KafkaTemplate<String, CardDTO> kafkaTemplate;

    public KafkaClientConsumer(KafkaTemplate<String, CardDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CardDTO card) {
        kafkaTemplate.sendDefault(card);
    }

    public void send(String topic, CardDTO card) {
        kafkaTemplate.send(topic, card);
    }
}