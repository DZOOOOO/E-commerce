package com.commerce.web.order.dto.response.message;

import lombok.Getter;

@Getter
public enum OrderMessage {
    ORDER_COMPLETE(Message.ORDER_COMPLETE),
    ORDER_CANCEL(Message.ORDER_CANCEL),
    ORDER_CONFIRM(Message.ORDER_CONFIRM);

    private final String message;

    OrderMessage(String message) {
        this.message = message;
    }

    static class Message {
        private final static String ORDER_COMPLETE = "주문완료.";
        private final static String ORDER_CANCEL = "주문취소.";
        private final static String ORDER_CONFIRM = "주문확정.";
    }
}
