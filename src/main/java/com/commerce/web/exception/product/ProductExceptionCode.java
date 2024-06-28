package com.commerce.web.exception.product;

import lombok.Getter;

@Getter
public enum ProductExceptionCode {
    NOT_FOUND(Message.NOT_FOUND),
    NOT_VALID(Message.NOT_VALID),
    ALREADY_REGISTER(Message.ALREADY_REGISTER);

    private final String message;

    ProductExceptionCode(String message) {
        this.message = message;
    }

    static class Message {
        private final static String NOT_FOUND = "없는 상품입니다..";
        private final static String NOT_VALID = "유효한 요청이 아닙니다.";
        private final static String ALREADY_REGISTER = "이미 등록된 상품입니다.";

    }
}
