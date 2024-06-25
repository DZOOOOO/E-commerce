package com.commerce.web.member.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPasswordUpdateRequestDto {

    private String currentPassword;
    private String confirmPassword;
    private String newPassword;
}
