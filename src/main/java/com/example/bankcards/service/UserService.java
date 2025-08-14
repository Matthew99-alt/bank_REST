package com.example.bankcards.service;

import com.example.bankcards.dto.SignupRequest;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CustomEntityNotFoundException;
import com.example.bankcards.exception.UnuniqueParameterException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс сервис отвечающий за реализацию запросов контроллера и работу с объектами класса CardUser
 * @see  User
 * @see  UsersRepository
 * @see  UserDTO
 * @see  UserMapper
 * @see  com.example.bankcards.controller.UserController
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    private final AuthService authService;

    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::makeAUserDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long encodedId) {
        User user = userRepository.findById(encodedId)
                .orElseThrow(() -> new CustomEntityNotFoundException("Указанный пользователь не найден"));
        return userMapper.makeAUserDTO(user);
    }

    @Transactional
    public UserDTO saveUser(UserDTO userDTO){

        if (userRepository.existsByEmail(userDTO.getEmail())
                ||userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())){
            throw new UnuniqueParameterException("Email and phone number should be unique");
        }

        User savedUser = userRepository.save(userMapper.makeAUser(userDTO));

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(userDTO.getFirstName()+userDTO.getMiddleName()+userDTO.getSecondName());
        signupRequest.setPassword(userDTO.getPassword());
        signupRequest.setEmail(userDTO.getEmail());
        signupRequest.setRole(userDTO.getRole());

        authService.registerUser(signupRequest);

        return userMapper.makeAUserDTO(savedUser);
    }

    @Transactional
    public void deleteCard(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public UserDTO editCard(UserDTO userDTO) {
        User user = userMapper.makeAUser(userDTO);
        userRepository.save(user);
        return userDTO;
    }
}
