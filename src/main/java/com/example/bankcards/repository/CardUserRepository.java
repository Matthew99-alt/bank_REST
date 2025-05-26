package com.example.bankcards.repository;

import com.example.bankcards.entity.CardUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardUserRepository extends JpaRepository<CardUser, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone);
}