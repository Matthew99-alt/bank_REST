package com.example.bankcards.service;

import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.entity.CardUser;
import com.example.bankcards.exception.CustomEntityNotFoundException;
import com.example.bankcards.exception.UnuniqueParameterException;
import com.example.bankcards.mapper.CardUserMapper;
import com.example.bankcards.repository.CardUserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Класс сервис отвечающий за реализацию запросов контроллера и работу с объектами класса Card
 * @see  CardUser
 * @see  CardUserRepository
 * @see  CardUserDTO
 * @see  CardUserMapper
 * @see  com.example.bankcards.controller.CardUserController
 */

@Service
public class CardUserService {

    private final CardUserRepository cardUserRepository;

    private final CardUserMapper cardsUserMapper;

    private Long id;

    private String firstName;

    private String secondName;

    private String middleName;

    private String email;

    private String roles;

    private String phoneNumber;

    @JsonIgnore
    private String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public CardUserService(Long id, String firstName, String secondName, String middleName, String email, String phoneNumber, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.middleName = middleName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
    }

    public CardUserService(CardUserRepository cardUserRepository, CardUserMapper cardUserMapper){
        this.cardUserRepository = cardUserRepository;
        this.cardsUserMapper = cardUserMapper;
    }



    @Transactional(readOnly = true)
    public List<CardUserDTO> findAllCardUsers() {
        return cardUserRepository.findAll()
                .stream()
                .map(cardsUserMapper::makeACardUserDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CardUserDTO findCardUserById(Long encodedId) {
        CardUser cardUser = cardUserRepository.findById(encodedId)
                .orElseThrow(() -> new CustomEntityNotFoundException("Указанный пользователь не найден"));
        return cardsUserMapper.makeACardUserDTO(cardUser);
    }

    @Transactional
    public CardUserDTO saveCardUser(CardUserDTO cardUserDTO){

        if (cardUserRepository.existsByEmail(cardUserDTO.getEmail())
                ||cardUserRepository.existsByPhoneNumber(cardUserDTO.getPhoneNumber())){
            throw new UnuniqueParameterException("Email and phone number should be unique");
        }

        CardUser savedCardUser = cardUserRepository.save(cardsUserMapper.makeACardUser(cardUserDTO));
        return cardsUserMapper.makeACardUserDTO(savedCardUser);
    }

    public static CardUserService build(CardUser user) {
        GrantedAuthority authorities =  new SimpleGrantedAuthority(user.getRole().toString());

        return new CardUserService(
                user.getId(),
                user.getFirstName(),
                user.getSecondName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getPhoneNumber(),
                authorities);
    }

    @Transactional
    public void deleteUserCard(Long id) {
        cardUserRepository.deleteById(id);
    }

    @Transactional
    public CardUserDTO editUserCard(CardUserDTO cardUserDTO) {
        CardUser cardUser = cardsUserMapper.makeACardUser(cardUserDTO);
        cardUserRepository.save(cardUser);
        return cardUserDTO;
    }
}
