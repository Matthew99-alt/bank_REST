package com.example.bankcards.service;

import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.Role;
import com.example.bankcards.exception.CustomEntityNotFoundException;
import com.example.bankcards.exception.UnuniqueParameterException;
import com.example.bankcards.mapper.CardUserMapper;
import com.example.bankcards.repository.CardUserRepository;
import com.example.bankcards.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Класс сервис отвечающий за реализацию запросов контроллера и работу с объектами класса CardUser
 * @see  User
 * @see  CardUserRepository
 * @see  CardUserDTO
 * @see  CardUserMapper
 * @see  com.example.bankcards.controller.CardUserController
 */

@Service
@RequiredArgsConstructor
public class CardUserService {
    //todo: ты работаешь тут с пользователями!
    // карты тут не нужны!, но если вдруг надо то можно сходить за ними

    private final CardUserRepository cardUserRepository;

    private final CardUserMapper cardsUserMapper;

    private final RoleRepository roleRepository;


    @Transactional(readOnly = true)
    public List<CardUserDTO> findAllCardUsers() {
        return cardUserRepository.findAll()
                .stream()
                .map(cardsUserMapper::makeACardUserDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CardUserDTO findCardUserById(Long encodedId) {
        User user = cardUserRepository.findById(encodedId)
                .orElseThrow(() -> new CustomEntityNotFoundException("Указанный пользователь не найден"));
        return cardsUserMapper.makeACardUserDTO(user);
    }

    @Transactional
    public CardUserDTO saveCardUser(CardUserDTO cardUserDTO){

        if (cardUserRepository.existsByEmail(cardUserDTO.getEmail())
                ||cardUserRepository.existsByPhoneNumber(cardUserDTO.getPhoneNumber())){
            throw new UnuniqueParameterException("Email and phone number should be unique");
        }

        User savedUser = cardUserRepository.save(cardsUserMapper.makeACardUser(cardUserDTO));

        Set<Role> roles = roleRepository.findAllById(cardUserDTO.getRoleIds());
        savedUser.setRole(roles);

        return cardsUserMapper.makeACardUserDTO(savedUser);
    }

    @Transactional
    public void deleteUserCard(Long id) {
        cardUserRepository.deleteById(id);
    }

    @Transactional
    public CardUserDTO editUserCard(CardUserDTO cardUserDTO) {
        User user = cardsUserMapper.makeACardUser(cardUserDTO);
        cardUserRepository.save(user);
        return cardUserDTO;
    }
}
