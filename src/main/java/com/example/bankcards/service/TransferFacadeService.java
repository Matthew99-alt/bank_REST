package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.producer.TransferEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferFacadeService {

    private final CardService cardService;
    private final TransferEventProducer producer;

    public TransactionDTO transferWithNotification(TransactionDTO transactionDTO) {
        TransactionDTO transfer = cardService.transfer(transactionDTO);
        producer.sendTransferEvent(transactionDTO);
        log.info("Transfer event sent to Kafka: {}", transfer);
        return transfer;
    }
}
