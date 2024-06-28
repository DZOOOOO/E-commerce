package com.commerce.web.order.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListResponseDto {


    private Long productId;
    private String productName;
    private Integer price;

}
