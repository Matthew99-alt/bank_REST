package com.example.bankcards.producer;

import com.example.bankcards.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferEventProducer {

    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;

    @Value("${t1.kafka.topic.transfer}")
    private String topic;

    public void sendTransferEvent(TransactionDTO event) {
        log.info("Sending transfer event: {}", event);
        kafkaTemplate.send(topic, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully to topic {}, offset {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send message", ex);
                    }
                });
    }
}