package com.example.bankcards.service;

import com.example.bankcards.dto.JwtResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.MessageResponse;
import com.example.bankcards.dto.SignupRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.Role;
import com.example.bankcards.repository.UsersRepository;
import com.example.bankcards.util.RoleEnum;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.util.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsersRepository userRepository; // TODO: обращайся в UserService
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }


    public MessageResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        // Create new user's account
        User user = userRepository.findByEmail(signUpRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Указанный пользователь не найден"));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                        //TODO: почему множественные запросы в базу??
                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

            });
        }

        user.setRole(roles);
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }
}
