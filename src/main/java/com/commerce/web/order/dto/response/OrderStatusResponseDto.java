package com.commerce.web.order.dto.response;

import com.commerce.domain.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusResponseDto {

    // 주문일자 (yyyy-MM-dd)
    private String orderDate;

    // 주문번호
    private Long orderId;

    // 주문상품
    private String productName;

    // 주문금액
    private Integer totalPrice;

    // 주문상태
    private OrderStatus orderStatus;

    // 구매 확정 여부
    private Boolean purchaseConfirm;
}
