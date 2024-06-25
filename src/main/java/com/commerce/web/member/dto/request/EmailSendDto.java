package com.commerce.web.member.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailSendDto {

    // 받는 사람 이메일.
    private String to;

    // 메일 제목
    private String subject;

    // 메일 내용.
    private String text;
}