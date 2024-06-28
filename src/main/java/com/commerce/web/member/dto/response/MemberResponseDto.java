package com.commerce.web.member.dto.response;

import com.commerce.web.member.dto.response.message.MemberMessage;
import lombok.*;

@Getter
@Setter
public class MemberResponseDto {

    private String message;

    @Builder
    public MemberResponseDto(MemberMessage message) {
        this.message = message.getMessage();
    }
}
