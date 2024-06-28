package com.commerce.web.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPasswordUpdateRequestDto {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String confirmPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String newPassword;
}
