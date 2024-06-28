package com.commerce.web.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCheckOutRequestDto {

    @Min(value = 0, message = "상품번호를 입력해주세요.")
    private Long productId; // 상품 번호

    @NotBlank(message = "상품이름을 입력해주세요.")
    private String productName; // 상품 이름

    @Min(value = 0, message = "상품수량을 입력해주세요.")
    private Integer productQuantity; // 상품 수량

    @Min(value = 0, message = "상품가격을 입력해주세요.")
    private Integer buyProductPrice; // 상품가격

}
