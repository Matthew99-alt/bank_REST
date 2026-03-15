package com.example.bankcards.consumer;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.dto.TransferEvent;
import com.example.bankcards.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${t1.kafka.topic.transfer}",
            containerFactory = "transferKafkaListenerContainerFactory")
    public void handleTransferEvent(TransactionDTO event, Acknowledgment ack) {
        log.info("Received transfer event: {}", event);
        try {
            notificationService.sendEmailNotification(event);
            ack.acknowledge();
            log.info("Event processed and acknowledged");
        } catch (Exception e) {
            log.error("Error processing transfer event: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}