package com.example.bankcards.controller;

import com.example.bankcards.entity.User;
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
public class UserTest {
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
        User user = makeACardUserForTests();

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

        User user = makeACardUserForTests();

        mockMvc.perform(
                        delete("/user/delete").param("id", user.getId().toString())
                )
                .andExpect(status().isOk());

        Assertions.assertFalse(cardRepository.findById(user.getId()).isPresent());
    }

    @Test
    @Transactional
    public void testEditUser() throws Exception {

        User user = makeACardUserForTests();

        String requestBody = "{\n" +
                "    \"id\":\"" + user.getId() + "\",\n" +
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

    private User makeACardUserForTests() {
        User user = new User();
        user.setPhoneNumber("+79540012325");
        user.setEmail("helloooooo@gmail.com");
        user.setFirstName("Павел");
        user.setMiddleName("Павлов");
        user.setSecondName("Павлович");

        return cardUserRepository.save(user);
    }
}
