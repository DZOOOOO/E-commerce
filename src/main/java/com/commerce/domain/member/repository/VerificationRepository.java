package com.commerce.domain.member.repository;

import com.commerce.domain.member.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Optional<Verification> findByEmail(String email);
    Optional<Verification> findByEmailAndToken(String email, String token);
    Optional<Verification> findByEmailAndTokenExpireAndEmailVerificationAndDeleteToken
            (String email, boolean expiry, boolean emailVerification, boolean deleteToken);
}
