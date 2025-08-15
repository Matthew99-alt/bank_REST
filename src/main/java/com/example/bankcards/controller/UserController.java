package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public List<UserDTO> getAllCardsUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/")
    public UserDTO getACardUser(@RequestParam("id") Long id) {
        return userService.findUserById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public void deleteACardUser(@RequestParam("id") Long id) {
        userService.deleteCard(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/edit")
    public UserDTO editACardUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.editCard(userDTO);
    }
}
