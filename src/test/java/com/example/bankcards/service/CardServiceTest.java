package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    void transfer_Success() {

        User user = makeAUser();

        Card card = new Card();
        card.setId(2L);
        card.setStatus(Status.ACTIVE);
        card.setUser(user);
        card.setBalance(10000L);
        card.setFinalDate(LocalDate.parse("2025-12-31"));

        Card card2 = makeACard(user);
        TransactionDTO transactionDTO = new TransactionDTO(2L, 1L, 1000L);

        UserDetailsImpl userDetails = makeUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(cardRepository.getReferenceById(1L)).thenReturn(card2);
        when(cardRepository.getReferenceById(2L)).thenReturn(card);

        TransactionDTO transactionDTO1 = cardService.transfer(transactionDTO, userDetails);

        assertEquals(9000L, card.getBalance());
        assertEquals(11000L, card2.getBalance());
    }

    @Test
    void transfer_InsufficientFunds() {

    }

    @Test
    void blockCard_AlreadyBlocked() {

    }

    @Test
    void transfer_ToBlockedCard() {

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

    private Card makeACard(User user) {

        Card card = new Card();
        card.setId(1L);
        card.setStatus(Status.ACTIVE);
        card.setUser(makeAUser());
        card.setBalance(10000L);
        card.setFinalDate(LocalDate.parse("2025-12-31"));

        return card;
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
}