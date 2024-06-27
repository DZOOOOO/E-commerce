package com.commerce.web.order.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListRegisterRequestDto {

    private Long memberId;
    private Long productId;

}
