package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.service.CardService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для запросов связанных с катрами
*/

@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/")
    public CardDTO getACard(@RequestParam("id") Long id) {
        return cardService.findCardById(id);
    }

    @GetMapping("/all")
    public List<CardDTO> getAllCards() {
        return cardService.findAllCards();
    }

    @GetMapping("/userId/")
    public List<CardDTO> getCardsByUserId(@RequestParam("id") Long id) {
        return cardService.findByUserId(id);
    }

    @PostMapping("/save")
    public CardDTO saveACard(@RequestBody @Valid CardDTO cardDTO) {
        return cardService.saveCard(cardDTO);
    }

    @PostMapping("/block/")
    public CardDTO blockCard(@RequestParam("id") Long cardId) {
        return cardService.blockCard(cardId);
    }

    @PostMapping("/activate/")
    public CardDTO activateCard(@RequestParam("id") Long cardId) {
        return cardService.activateCard(cardId);
    }

    @PostMapping("/outdated/")
    public CardDTO outdateCard(@RequestParam("id") Long cardId) {
        return cardService.outdateCard(cardId);
    }

    @DeleteMapping("/delete")
    public void deleteACard(@RequestParam("id") Long id) {
        cardService.deleteCard(id);
    }

    @PutMapping("/edit")
    public CardDTO editACard(@RequestBody @Valid CardDTO cardDTO) {
        return cardService.editCard(cardDTO);
    }

    @PostMapping("/transaction")
    public TransactionDTO doTransaction(@RequestBody TransactionDTO transactionDTO){
        return cardService.doTransaction(transactionDTO);
    }
}
