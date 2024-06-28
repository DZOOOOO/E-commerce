package com.commerce.web.exception.order;

import lombok.Getter;

@Getter
public enum OrderExceptionCode {
    NOT_FOUND(Message.NOT_FOUND),
    NOT_VALID(Message.NOT_VALID),
    ORDER_ALREADY(Message.ORDER_ALREADY),
    ORDER_NOT_CANCEL(Message.ORDER_NOT_CANCEL);

    private final String message;

    OrderExceptionCode(String message) {
        this.message = message;
    }

    static class Message {
        private final static String NOT_FOUND = "없는 주문입니다.";
        private final static String NOT_VALID = "유효한 요청이 아닙니다.";
        private final static String ORDER_ALREADY = "이미 주문한 제품입니다.";
        private final static String ORDER_NOT_CANCEL = "구매확정 상품은 취소할 수 없습니다.";


    }
}
