package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendEmailNotification(TransactionDTO event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.email());
        message.setSubject("Подтверждение перевода средств");
        message.setText(String.format(
                "Здравствуйте!\n\n" +
                        "Перевод на сумму %.2f с карты %s на карту %s успешно выполнен.\n" +
                        "Дата и время операции: %s\n\n" +
                        "С уважением, команда банка.",
                event.amount(),
                maskCardNumber(event.fromCardId().toString()),
                maskCardNumber(event.toCardId().toString()),
                event.email()
        ));

        try {
            mailSender.send(message);
            log.info("Email sent to {}", event.email());
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
            // Можно пробросить исключение, чтобы консьюмер повторил попытку
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return cardNumber;
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}