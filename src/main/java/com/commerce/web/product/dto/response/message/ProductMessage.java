package com.commerce.web.product.dto.response.message;

import lombok.Getter;

@Getter
public enum ProductMessage {
    PRODUCT_REGISTER(Message.PRODUCT_REGISTER);

    private final String message;

    ProductMessage(String message) {
        this.message = message;
    }

    static class Message {
        private final static String PRODUCT_REGISTER = "상품등록완료.";
    }
}
