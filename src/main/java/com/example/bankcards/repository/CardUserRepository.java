package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий отвечающий за работу с таблицей указанной в сущности CardUser
 * @see  CardUser
*/
@Repository
public interface CardUserRepository extends JpaRepository<CardUser, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone);
    Optional<CardUser> findByEmail(String email);
}