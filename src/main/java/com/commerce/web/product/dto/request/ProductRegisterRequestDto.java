package com.commerce.web.product.dto.request;

import com.commerce.domain.product.entity.ProductCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRegisterRequestDto {

    private String productName;
    private Integer productPrice;
    private Integer productStock;
    private String productDescription;
    private ProductCategory productCategory;

}
