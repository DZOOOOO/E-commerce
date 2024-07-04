package com.commerce.domain.member.entity.cache;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CacheVerification {
    // 토큰
    private String token;
    // 토큰 유효시간 - 5분이내
    private String expiryDate;
    // 토큰 만료 체크
    private boolean tokenExpire;
    // 이메일 인증 여부 체크
    private boolean emailVerification;
    // 토큰 죽음 여부
    private boolean deleteToken;

    public static CacheVerification cacheToken(String token, String expiryDate,
                                               boolean tokenExpire, boolean emailVerification,
                                               boolean deleteToken) {
        return new CacheVerification(token, expiryDate, tokenExpire, emailVerification, deleteToken);
    }
}
