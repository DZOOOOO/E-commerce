package com.commerce.web.exception.product;

public class ProductException extends RuntimeException {
    public ProductException(ProductExceptionCode message) {
        super(message.getMessage());
    }
}
