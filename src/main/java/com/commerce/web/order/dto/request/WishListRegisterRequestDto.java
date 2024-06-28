package com.commerce.web.order.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListRegisterRequestDto {

    @Min(value = 0, message = "회원정보를 입력해주세요.")
    private Long memberId;

    @Min(value = 0, message = "상품정보를 입력해주세요.")
    private Long productId;

}
