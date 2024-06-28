package com.commerce.web.exception.order;

public class OrderException extends RuntimeException {
    public OrderException(OrderExceptionCode message) {
        super(message.getMessage());
    }
}
