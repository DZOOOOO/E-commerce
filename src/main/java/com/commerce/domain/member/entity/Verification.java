package com.commerce.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "token", nullable = false)
    private String token;

    // 토큰 만료기간(5분)
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "token_expire", nullable = false)
    private boolean tokenExpire;

    @Column(name = "email_verification", nullable = false)
    private boolean emailVerification;

    @Column(name = "delete")
    private boolean delete;

    // 토큰 생성 메서드
    public static Verification createVerification(String email,
                                                  String token,
                                                  LocalDateTime expiryDate,
                                                  boolean tokenExpire,
                                                  boolean emailVerification,
                                                  boolean delete) {
        return Verification.builder()
                .email(email)
                .token(token)
                .expiryDate(expiryDate)
                .tokenExpire(tokenExpire)
                .emailVerification(emailVerification)
                .delete(delete)
                .build();
    }

    // 토큰 만료 처리 메서드
    public void emailTokenKill(boolean tokenExpire, boolean emailVerification, boolean delete) {
        this.tokenExpire = tokenExpire;
        this.emailVerification = emailVerification;
        this.delete = delete;
    }

    public void updateEmailTokenStatus(boolean tokenExpire, boolean emailVerification) {
        this.tokenExpire = tokenExpire;
        this.emailVerification = emailVerification;
    }

    // 토큰 최신화
    public void updateToken(String newToken) {
        this.token = newToken;
    }
}




