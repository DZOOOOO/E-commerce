package com.commerce.web.order.dto.response.message;

import lombok.Getter;

@Getter
public enum WishListMessage {
    WISH_LIST_REGISTER(Message.WISH_LIST_REGISTER),
    WISH_LIST_DELETE(Message.WISH_LIST_DELETE);

    private final String message;

    WishListMessage(String message) {
        this.message = message;
    }

    static class Message {
        private final static String WISH_LIST_REGISTER = "WishList 등록완료.";
        private final static String WISH_LIST_DELETE = "삭제완료.";
    }
}
