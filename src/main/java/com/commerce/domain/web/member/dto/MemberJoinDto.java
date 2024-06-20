package com.commerce.domain.web.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberJoinDto {

    @Email(message = "유효하지 않은 이메일 입니다.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "핸드폰 번호를 작성해주세요.")
    private String phone;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
    
    private boolean emailCheck = false;
}
