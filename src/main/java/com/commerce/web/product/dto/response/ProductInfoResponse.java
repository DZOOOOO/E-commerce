package com.commerce.web.product.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInfoResponse {

    private Long productId;
    private String productName;
    private Integer productPrice;
    private Integer productStock;
    private String productDescription;
    private Boolean productPurchaseStatus;

    // 구매 가능 날짜 + 시간 추가 예정.
}
