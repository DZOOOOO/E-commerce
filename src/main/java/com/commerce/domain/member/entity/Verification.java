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

    @Column(name = "email")
    private String email;

    @Column(name = "token")
    private String token;

    // 토큰 만료기간(5분)
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "token_expire")
    private boolean tokenExpire;

    @Column(name = "email_verification")
    private boolean emailVerification;

    @Column(name = "delete_token")
    private boolean deleteToken;

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
                .deleteToken(delete)
                .build();
    }

    // 토큰 만료 처리 메서드
    public void emailTokenKill(boolean tokenExpire, boolean emailVerification, boolean delete) {
        this.tokenExpire = tokenExpire;
        this.emailVerification = emailVerification;
        this.deleteToken = delete;
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




