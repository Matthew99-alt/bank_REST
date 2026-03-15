package com.example.bankcards.producer;

import com.example.bankcards.dto.CardDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClientProducer {
    private final KafkaTemplate<String, CardDTO> kafkaTemplate;

    public KafkaClientProducer(KafkaTemplate<String, CardDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CardDTO card) {
        kafkaTemplate.sendDefault(card);
    }

    public void send(String topic, CardDTO card) {
        kafkaTemplate.send(topic, card);
    }
}