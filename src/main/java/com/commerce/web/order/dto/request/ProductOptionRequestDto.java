package com.commerce.web.order.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionRequestDto {

    @Min(value = 0, message = "상품수량을 입력해주세요.")
    private Integer productQuantity;

    @Min(value = 0, message = "총 가격을 입력하세요.")
    private Integer totalPrice;

}
