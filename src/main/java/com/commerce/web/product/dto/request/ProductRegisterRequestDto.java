package com.commerce.web.product.dto.request;

import com.commerce.domain.product.entity.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRegisterRequestDto {

    @NotBlank(message = "상품명을 입력해주세요.")
    private String productName;

    @Min(value = 0, message = "상품 가격을 입력해주세요.")
    private Integer productPrice;

    @Min(value = 0, message = "상품 재고를 입력해주세요.")
    private Integer productStock;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String productDescription;

    @NotBlank(message = "상품 종류를 입력해주세요.")
    private ProductCategory productCategory;

}
