package com.commerce.web.exception.wishlist;

public class WishlistException extends RuntimeException {
    public WishlistException(WishListExceptionCode message) {
        super(message.getMessage());
    }
}
