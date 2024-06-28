package com.commerce.web.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPageViewResponseDto {

    private Long memberId; // 주문자
    private Long productId; // 상품 번호
    private String productName; // 상품 이름
    private Integer productQuantity; // 상품 수량
    private Integer productPrice; // 상품 가격
    private Integer totalPrice; // 총 가격

}
