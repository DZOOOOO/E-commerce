package com.commerce.web.member.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoResponse {

    private String email;
    private String name;
    private String phone;
    private String address;
    private String role;

}
