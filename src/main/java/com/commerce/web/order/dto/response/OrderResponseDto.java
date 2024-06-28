package com.commerce.web.order.dto.response;

import com.commerce.web.order.dto.response.message.OrderMessage;
import lombok.*;

@Getter
@Setter
public class OrderResponseDto {

    private String message;

    @Builder
    public OrderResponseDto(OrderMessage message) {
        this.message = message.getMessage();
    }
}
