package com.example.bankcards.service;

import com.example.bankcards.entity.CardUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CardUserDetailsFactory {
    public CardUserDetailsImpl build(CardUser user) {
        List<GrantedAuthority> authorities = user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new CardUserDetailsImpl(
                user.getId(),
                user.getFirstName()+" "+user.getSecondName()+" "+user.getMiddleName(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getPhoneNumber()
        );
    }
}