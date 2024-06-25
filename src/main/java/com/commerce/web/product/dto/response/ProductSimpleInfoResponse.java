package com.commerce.web.product.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSimpleInfoResponse {

    private Long productId;
    private String productName;
    private Integer productPrice;
    private Integer productStock;
}
