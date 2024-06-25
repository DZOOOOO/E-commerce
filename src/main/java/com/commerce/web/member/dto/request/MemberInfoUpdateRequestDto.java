package com.commerce.web.member.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoUpdateRequestDto {

    private String address;
    private String phone;

}
