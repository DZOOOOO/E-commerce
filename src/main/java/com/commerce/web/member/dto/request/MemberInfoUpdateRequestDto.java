package com.commerce.web.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoUpdateRequestDto {

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;

}
