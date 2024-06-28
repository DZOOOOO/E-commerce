package com.commerce.web.member.dto.response;

import com.commerce.web.order.dto.response.WishListResponseDto;
import lombok.*;

import java.util.List;

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
    private List<WishListResponseDto> wishList;
}
