package com.commerce.domain.member.repository;

import com.commerce.domain.member.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Optional<Verification> findByEmail(String email);
    Optional<Verification> findByEmailAndToken(String email, String token);
    Optional<Verification> findByEmailAndTokenExpireAndEmailVerificationAndDelete
            (String email, boolean expiry, boolean emailVerification, boolean delete);
}
