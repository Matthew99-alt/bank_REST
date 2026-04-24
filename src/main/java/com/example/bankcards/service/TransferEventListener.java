package com.example.bankcards.service;

import com.example.bankcards.producer.TransferEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.example.bankcards.service.CardService.TransferCompletedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferEventListener {

    private final TransferEventProducer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void transferWithNotification(TransferCompletedEvent completedEvent) {
        producer.sendTransferEvent(completedEvent.transaction());
        log.info("Transfer event sent to Kafka: {}", completedEvent.transaction());
    }
}
