package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для запросов связанных с катрами
*/

@RestController
@RequestMapping("/cards")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/")
    public List<CardDTO> getAllCards() {
        return cardService.findAllCards();
    }

    @GetMapping("/{id}")
    public CardDTO getACard(@PathVariable("id") Long id) {
        return cardService.findCardById(id);
    }

    @GetMapping("/userId")
    public List<CardDTO> getCardsByUserId(@RequestParam("id") Long id) {
        return cardService.findByUserId(id);
    }

    @PostMapping("/")
    public CardDTO saveACard(@RequestBody @Valid CardDTO cardDTO) {
        return cardService.saveCard(cardDTO);
    }

    @PatchMapping("/{id}/block")
    public CardDTO blockCard(@PathVariable("id") Long cardId) {
        return cardService.blockCard(cardId);
    }

    @PatchMapping("/{id}/activate")
    public CardDTO activateCard(@PathVariable("id") Long cardId) {
        return cardService.activateCard(cardId);
    }

    @DeleteMapping("/delete") // todo: а нужен ли?
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteACard(@RequestParam("id") Long id) {
        cardService.deleteCard(id);
    }

    @PutMapping("/edit") // todo: а нужен ли?
    public CardDTO editACard(@RequestBody @Valid CardDTO cardDTO) {
        return cardService.editCard(cardDTO);
    }

    @PostMapping("/transfer")
    public TransactionDTO internalCardTransfer(@RequestBody TransactionDTO transactionDTO){
        return cardService.internalCardTransfer(transactionDTO);
    }
}
