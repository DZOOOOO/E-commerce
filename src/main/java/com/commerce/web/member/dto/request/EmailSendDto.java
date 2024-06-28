package com.commerce.web.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailSendDto {

    // 받는 사람 이메일.
    @Email(message = "이메일을 작성해주세요.")
    private String to;

    // 메일 제목
    @NotBlank(message = "메일 제목을 작성해주세요.")
    private String subject;

    // 메일 내용.
    @NotBlank(message = "메일 내용을 작성해주세요.")
    private String text;
}
