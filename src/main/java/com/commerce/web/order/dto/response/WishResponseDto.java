package com.commerce.web.order.dto.response;

import com.commerce.web.order.dto.response.message.WishListMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishResponseDto {

    private String message;

    @Builder
    public WishResponseDto(WishListMessage message) {
        this.message = message.getMessage();
    }
}
