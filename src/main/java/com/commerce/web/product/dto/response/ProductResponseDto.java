package com.commerce.web.product.dto.response;

import com.commerce.web.product.dto.response.message.ProductMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {

    private String message;

    @Builder
    public ProductResponseDto(ProductMessage message) {
        this.message = message.getMessage();
    }
}
