package com.example.bankcards.controller;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardUser;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardUserRepository;
import com.example.bankcards.util.Status;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardUserRepository cardUserRepository;

    @BeforeEach
    void setUp() {
        cardRepository.deleteAll();
        cardUserRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testGetAllCards() throws Exception {
        mockMvc.perform(
                        get("/card/all")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testGetAllCardsByUserId() throws Exception {
        Card card = makeACardForTests();

        mockMvc.perform(
                        get("/card/userId/").param("id", card.getId().toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testGetCardById() throws Exception {
        Card card = makeACardForTests();

        mockMvc.perform(
                        get("/card/").param("id", card.getId().toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testSaveCard() throws Exception {

        Card card = makeACardForTests();

        String requestBody = "{\n" +
                "    \"finalDate\": \"2025-11-11\",\n" +
                "    \"status\": \"ACTIVE\",\n" +
                "    \"balance\": 100000,\n" +
                "    \"userId\":" + card.getUser().getId() + "\n" +
                "}";

        mockMvc.perform(
                        post("/card/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    void blockCard_ShouldReturnBlockedCard() throws Exception {

        Card card = makeACardForTests();

        mockMvc.perform(
                        post("/card/block/").param("id", card.getId().toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Card updatedCard = cardRepository.findById(card.getId()).orElseThrow();
        Assertions.assertEquals(Status.BLOCKED, updatedCard.getStatus());
    }

    @Test
    @Transactional
    void activeCard_ShouldReturnActive() throws Exception {

        Card card = makeACardForTests();

        card.setStatus(Status.BLOCKED);

        mockMvc.perform(
                        post("/card/activate/").param("id", card.getId().toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Card updatedCard = cardRepository.findById(card.getId()).orElseThrow();
        Assertions.assertEquals(Status.ACTIVE, updatedCard.getStatus());
    }

    @Test
    @Transactional
    void outdateCard_ShouldReturnOutdated() throws Exception {

        Card card = makeACardForTests();

        mockMvc.perform(
                        post("/card/outdated/").param("id", card.getId().toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Card updatedCard = cardRepository.findById(card.getId()).orElseThrow();
        Assertions.assertEquals(Status.OUTDATED, updatedCard.getStatus());
    }


    @Test
    @Transactional
    public void testDeleteCard() throws Exception {

        Card card = makeACardForTests();

        mockMvc.perform(
                        delete("/card/delete").param("id", card.getId().toString())
                )
                .andExpect(status().isOk());

        Assertions.assertFalse(cardRepository.findById(card.getId()).isPresent());
    }


    @Test
    @Transactional
    public void testEditCard() throws Exception {

        Card savedCard = makeACardForTests();

        String requestBody = "{\n" +
                "    \"id\":\"" + savedCard.getId() + "\",\n" +
                "    \"finalDate\": \"2025-11-11\",\n" +
                "    \"status\": \"ACTIVE\",\n" +
                "    \"balance\": 100000,\n" +
                "    \"userId\": " + savedCard.getUser().getId() +  "\n" +
                "}";

        mockMvc.perform(
                        put("/card/edit").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testTransferCard() throws Exception {

        TransactionDTO transactionDTO = makeATransactionDTOForTests();

        String requestBody = "{\n" +
                "    \"fromCardId\":"+ transactionDTO.getToCardId() +",\n" +
                "    \"toCardId\": "+ transactionDTO.getFromCardId() +",\n" +
                "    \"amount\": 1000\n" +
                "}";

        mockMvc.perform(
                        post("/card/transaction").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testTransferCard_sameCards() throws Exception {

        TransactionDTO transactionDTO = makeATransactionDTOForTests();

        String requestBody = "{\n" +
                "    \"fromCardId\":"+ transactionDTO.getToCardId() +",\n" +
                "    \"toCardId\": "+ transactionDTO.getToCardId() +",\n" +
                "    \"amount\": 1000\n" +
                "}";

        mockMvc.perform(
                        post("/card/transaction").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testTransferCard_differentUsers() throws Exception {
        TransactionDTO transactionDTO = makeATransactionDTOForTests();

        CardUser cardUser = new CardUser();
        cardUser.setPhoneNumber("+79740012325");
        cardUser.setEmail("hellohello@gmail.com");
        cardUser.setFirstName("Павел");
        cardUser.setMiddleName("Павлов");
        cardUser.setSecondName("Павлович");

        cardUserRepository.save(cardUser);

        Card card = new Card();

        card.setStatus(Status.ACTIVE);
        card.setUser(cardUser);
        card.setBalance(10000L);
        card.setFinalDate(LocalDate.parse("2025-12-31"));

        card = cardRepository.save(card);

        String requestBody = "{\n" +
                "    \"fromCardId\":"+ card.getId() +",\n" +
                "    \"toCardId\": "+ transactionDTO.getFromCardId() +",\n" +
                "    \"amount\": 1000\n" +
                "}";

        mockMvc.perform(
                        post("/card/transaction").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testTransferCard_negativeAmount() throws Exception {

        TransactionDTO transactionDTO = makeATransactionDTOForTests();

        String requestBody = "{\n" +
                "    \"fromCardId\":"+ transactionDTO.getToCardId() +",\n" +
                "    \"toCardId\": "+ transactionDTO.getFromCardId() +",\n" +
                "    \"amount\": -1000\n" +
                "}";

        mockMvc.perform(
                        post("/card/transaction").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testTransferCard_unactiveCard() throws Exception {

        TransactionDTO transactionDTO = makeATransactionDTOForTests();

        Card card = cardRepository.getReferenceById(transactionDTO.getToCardId());

        card.setStatus(Status.BLOCKED);

        card = cardRepository.save(card);

        String requestBody = "{\n" +
                "    \"fromCardId\":"+ card.getId() +",\n" +
                "    \"toCardId\": "+ transactionDTO.getFromCardId() +",\n" +
                "    \"amount\": 1000\n" +
                "}";

        mockMvc.perform(
                        post("/card/transaction").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    private Card makeACardForTests() {
        Card card = new Card();
        CardUser cardUser = makeACardUserForTests();


        card.setStatus(Status.ACTIVE);
        card.setUser(cardUser);
        card.setBalance(10000L);
        card.setFinalDate(LocalDate.parse("2025-12-31"));

        return cardRepository.save(card);
    }
    private CardUser makeACardUserForTests() {
        CardUser cardUser = new CardUser();
        cardUser.setPhoneNumber("+79540012325");
        cardUser.setEmail("hellothere@gmail.com");
        cardUser.setFirstName("Павел");
        cardUser.setMiddleName("Павлов");
        cardUser.setSecondName("Павлович");

        return cardUserRepository.save(cardUser);
    }

    private TransactionDTO makeATransactionDTOForTests(){
        TransactionDTO transactionDTO = new TransactionDTO();
        Card card1 = makeACardForTests();
        Card card = new Card();
        card.setStatus(Status.ACTIVE);
        card.setUser(card1.getUser());
        card.setBalance(10000L);
        card.setFinalDate(LocalDate.parse("2025-12-31"));
        cardRepository.save(card);

        transactionDTO.setToCardId(card.getId());
        transactionDTO.setFromCardId(card1.getId());
        transactionDTO.setAmount(100000L);

        return transactionDTO;
    }
}
