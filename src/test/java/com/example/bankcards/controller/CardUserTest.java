package com.example.bankcards.controller;

import com.example.bankcards.entity.CardUser;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardUserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CardUserTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardUserRepository cardUserRepository;

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        cardRepository.deleteAll();
        cardUserRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testGetAllCardUsers() throws Exception {
        mockMvc.perform(
                        get("/user/all")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testGetCardUserById() throws Exception {
        CardUser user = makeACardUserForTests();

        mockMvc.perform(
                        get("/user/").param("id", user.getId().toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testSaveCardUser() throws Exception {

        String requestBody = "{\n" +
                "    \"firstName\": \"Иван\",\n" +
                "    \"secondName\": \"Иванов\",\n" +
                "    \"middleName\": \"Иванович\",\n" +
                "    \"email\": \"ivanov123@example.com\",\n" +
                "    \"phoneNumber\": \"+79391934567\"\n" +
                "}";

        mockMvc.perform(
                        post("/user/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Transactional
    public void testDeleteUser() throws Exception {

        CardUser cardUser = makeACardUserForTests();

        mockMvc.perform(
                        delete("/user/delete").param("id", cardUser.getId().toString())
                )
                .andExpect(status().isOk());

        Assertions.assertFalse(cardRepository.findById(cardUser.getId()).isPresent());
    }

    @Test
    @Transactional
    public void testEditUser() throws Exception {

        CardUser cardUser = makeACardUserForTests();

        String requestBody = "{\n" +
                "    \"id\":\"" + cardUser.getId() + "\",\n" +
                "    \"firstName\": \"Иган\",\n" +
                "    \"secondName\": \"Иганов\",\n" +
                "    \"middleName\": \"Иванович\",\n" +
                "    \"email\": \"ivanov21@example.com\",\n" +
                "    \"phoneNumber\": \"+79391233567\"\n" +
                "}";

        mockMvc.perform(
                        put("/user/edit").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    private CardUser makeACardUserForTests() {
        CardUser cardUser = new CardUser();
        cardUser.setPhoneNumber("+79540012325");
        cardUser.setEmail("helloooooo@gmail.com");
        cardUser.setFirstName("Павел");
        cardUser.setMiddleName("Павлов");
        cardUser.setSecondName("Павлович");

        return cardUserRepository.save(cardUser);
    }
}
