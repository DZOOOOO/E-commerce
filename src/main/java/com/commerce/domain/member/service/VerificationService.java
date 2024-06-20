package com.commerce.domain.member.service;

import com.commerce.domain.member.entity.Verification;
import com.commerce.domain.member.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;

    @Transactional
    public Verification findByEmail(String email) {
        return verificationRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("찾는 토큰이 없습니다."));
    }

    // 발송 메일 체크
    @Transactional
    public boolean checkEmail(String email, String token, boolean valid) {
        Verification target = verificationRepository.findByEmail(email).orElse(null);
        if ((valid && target == null) || (target != null && target.isTokenExpire())) {
            Verification verification = Verification.builder()
                    .email(email)
                    .token(token)
                    .expiryDate(LocalDateTime.now())
                    .emailVerification(true)
                    .tokenExpire(false)
                    .build();
            verificationRepository.save(verification);
            return true;
        }
        return false;
//        throw new IllegalArgumentException("이미 토큰이 존재합니다.");
    }

    // 토큰 유효성 체크
    @Transactional(readOnly = true)
    public boolean findVeri(String email) {
        Verification target = verificationRepository.findByEmail(email).orElse(null);
        if (target != null) {
            LocalDateTime tokenTime = target.getExpiryDate().plusMinutes(1);
            // 메일인증 5분 이내로 회원가입 가능.
            if (tokenTime.isAfter(LocalDateTime.now())) {
                return true;
            } else {
                // 토큰 유효시간 지남.
                updateTokenExpire(target);
            }
        }
        return false;
    }

    private void updateTokenExpire(Verification target) {
        target.updateTokenExpire(true);
        verificationRepository.save(target);
    }
}
