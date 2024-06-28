package com.commerce.web.product.controller;

import com.commerce.domain.product.service.ProductService;
import com.commerce.web.product.dto.request.ProductRegisterRequestDto;
import com.commerce.web.product.dto.response.ProductInfoResponse;
import com.commerce.web.product.dto.response.ProductPageResponse;
import com.commerce.web.product.dto.response.ProductResponseDto;
import com.commerce.web.product.dto.response.ProductSimpleInfoResponse;
import com.commerce.web.product.dto.response.message.ProductMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 등록 API
    @PostMapping("/register")
    public ResponseEntity<?> registerProduct(@Valid @RequestBody ProductRegisterRequestDto dto) {
        productService.productRegister(dto);
        return new ResponseEntity<>(ProductResponseDto.builder()
                .message(ProductMessage.PRODUCT_REGISTER)
                .build(), HttpStatus.OK);
    }

    // 상품 리스트 조회 API
    @GetMapping("/list")
    public ResponseEntity<?> getProducts(@Positive @RequestParam int page,
                                         @Positive @RequestParam int size) {
        ProductPageResponse<ProductSimpleInfoResponse> productList
                = productService.getProductList(page - 1, size);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    // 상품 상세 조회 API
    @GetMapping("/detail/{productId}")
    public ResponseEntity<?> detailProduct(@Positive @PathVariable("productId") Long productId) {
        ProductInfoResponse target = productService.getProductInfo(productId);
        return new ResponseEntity<>(target, HttpStatus.OK);
    }
}

