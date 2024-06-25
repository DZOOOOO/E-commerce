package com.commerce.web.member.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {
    // JWT 반환.
    private String token;
}
