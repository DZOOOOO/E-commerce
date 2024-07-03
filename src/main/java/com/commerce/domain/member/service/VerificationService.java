package com.commerce.domain.member.service;

import com.commerce.domain.member.entity.Verification;
import com.commerce.domain.member.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j(topic = "메일 인증")
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;

    @Transactional(readOnly = true)
    public Optional<Verification> findByEmail(String email) {
        return verificationRepository.findByEmail(email);
    }

    // 발송 메일 체크
    @Transactional
    public boolean checkEmail(String email, String token, boolean valid) {
        // 이메일, 인증 토큰으로 조회
        Verification target = verificationRepository
                .findByEmailAndToken(email, token)
                .orElse(null);
        if (target != null
                && target.getToken().equals(token)
                && !target.isTokenExpire()
                && !target.isEmailVerification()
                && !target.isDeleteToken()) {
            // 토큰 상태 업데이트.
            updateTokenExpire(target);
            return true;
        }
        return false;
    }

    // 토큰 유효성 체크
    @Transactional(readOnly = true)
    public boolean findVeri(String email) {
        Verification target = verificationRepository
                .findByEmailAndTokenExpireAndEmailVerificationAndDeleteToken(email, true, true, false)
                .orElse(null);

        if (target != null) {
            // 메일인증 5분 이내로 회원가입 가능. -> 5분 지나면 토큰 만료.
            LocalDateTime tokenTime = target.getExpiryDate().plusMinutes(5);
            if (tokenTime.isAfter(LocalDateTime.now())) {
                return true;
            } else {
                log.info("토큰 유효기간 만료.");
                updateTokenKill(target);
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Verification findValidToken(String email,
                                       boolean expiry,
                                       boolean verification,
                                       boolean delete) {
        return verificationRepository
                .findByEmailAndTokenExpireAndEmailVerificationAndDeleteToken(email, expiry, verification, delete)
                .orElse(null);
    }

    // 토큰 상태 업데이트
    public void updateTokenExpire(Verification target) {
        target.updateEmailTokenStatus(true, true);
        verificationRepository.save(target);
    }

    // 토큰 만료.
    public void updateTokenKill(Verification target) {
        target.emailTokenKill(true, true, true);
        verificationRepository.save(target);
    }
}
