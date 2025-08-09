package com.example.bankcards.controller;

import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для запросов о пользователе
 */

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public List<CardUserDTO> getAllCardsUsers() {
        return userService.findAllCardUsers();
    }

    @GetMapping("/")
    public CardUserDTO getACardUser(@RequestParam("id") Long id) {
        return userService.findCardUserById(id);
    }

    @Operation(method = "POST")  // Явно указываем метод для Swagger
    @PostMapping("/save")
    public CardUserDTO saveACardUser(@RequestBody @Valid CardUserDTO cardUserDTO) {
        return userService.saveCardUser(cardUserDTO);
    }

    @DeleteMapping("/delete")
    public void deleteACardUser(@RequestParam("id") Long id) {
        userService.deleteUserCard(id);
    }

    @PutMapping("/edit")
    public CardUserDTO editACardUser(@RequestBody @Valid CardUserDTO cardUserDTO) {
        return userService.editUserCard(cardUserDTO);
    }
}
