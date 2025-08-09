package com.example.bankcards.service;

import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.Role;
import com.example.bankcards.exception.CustomEntityNotFoundException;
import com.example.bankcards.exception.UnuniqueParameterException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Класс сервис отвечающий за реализацию запросов контроллера и работу с объектами класса CardUser
 * @see  User
 * @see  UsersRepository
 * @see  CardUserDTO
 * @see  UserMapper
 * @see  com.example.bankcards.controller.UserController
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;


    @Transactional(readOnly = true)
    public List<CardUserDTO> findAllCardUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::makeACardUserDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CardUserDTO findCardUserById(Long encodedId) {
        User user = userRepository.findById(encodedId)
                .orElseThrow(() -> new CustomEntityNotFoundException("Указанный пользователь не найден"));
        return userMapper.makeACardUserDTO(user);
    }

    @Transactional
    public CardUserDTO saveCardUser(CardUserDTO cardUserDTO){

        if (userRepository.existsByEmail(cardUserDTO.getEmail())
                ||userRepository.existsByPhoneNumber(cardUserDTO.getPhoneNumber())){
            throw new UnuniqueParameterException("Email and phone number should be unique");
        }

        User savedUser = userRepository.save(userMapper.makeACardUser(cardUserDTO));

        Set<Role> roles = roleRepository.findAllById(cardUserDTO.getRoleIds());
        savedUser.setRole(roles);

        return userMapper.makeACardUserDTO(savedUser);
    }

    @Transactional
    public void deleteUserCard(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public CardUserDTO editUserCard(CardUserDTO cardUserDTO) {
        User user = userMapper.makeACardUser(cardUserDTO);
        userRepository.save(user);
        return cardUserDTO;
    }
}
