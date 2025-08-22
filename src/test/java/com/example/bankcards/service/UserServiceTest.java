package com.example.bankcards.service;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UsersRepository;
import com.example.bankcards.util.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findUserById_Success() {

    }

    @Test
    void findUserById_NotFound() {

    }

    @Test
    void deleteUser_Success() {

    }

    @Test
    void deleteUser_NotFound() {

    }

    @Test
    void editUser_Success() {

    }

    @Test
    void editUser_EmailAlreadyExists() {

    }

    @Test
    void createUser_Success() {

    }

    @Test
    void createUser_EmailAlreadyExists() {

    }

    //private User createTestUser() {
    //
    //}
}