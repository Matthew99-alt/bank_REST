package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/api/kafka")
public class KafkaTestController {

    private final KafkaTemplate<String, CardDTO> kafkaTemplate;

    @Autowired
    public KafkaTestController(KafkaTemplate<String, CardDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody CardDTO card) {
        kafkaTemplate.send("client-topic", card);
        return "Message sent to Kafka";
    }
}