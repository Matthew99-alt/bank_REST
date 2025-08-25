package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UsersRepository;
import com.example.bankcards.util.RoleEnum;
import com.example.bankcards.util.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    @Test
    void findAllCardsByUserId() {

    }

    private UserDetailsImpl makeUserDetails(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getFirstName() + user.getMiddleName() + user.getSecondName(),
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                user.getPhoneNumber()
        );

    }

    private User makeAUser() {
        User user = new User();
        user.setId(1L);
        user.setPhoneNumber("+79540012325");
        user.setEmail("hellothere@gmail.com");
        user.setFirstName("Павел");
        user.setMiddleName("Павлов");
        user.setSecondName("Павлович");
        user.setPassword("securepassword113");
        HashSet<Role> roles = new HashSet<>();
        roles.add(new Role(1L, RoleEnum.ROLE_ADMIN));
        user.setRole(roles);

        return user;
    }
}