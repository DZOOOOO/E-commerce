package com.commerce.web.exception.wishlist;

import lombok.Getter;

@Getter
public enum WishListExceptionCode {
    WISH_LIST_NOT_FOUND(Message.NOT_FOUND);

    private final String message;

    WishListExceptionCode(String message) {
        this.message = message;
    }

    static class Message {
        private final static String NOT_FOUND = "없는 정보입니다.";
    }
}
