package com.commerce.web.product.web.controller;

import com.commerce.domain.product.service.ProductService;
import com.commerce.web.product.dto.request.ProductRegisterRequestDto;
import com.commerce.web.product.dto.response.ProductInfoResponse;
import com.commerce.web.product.dto.response.ProductPageResponse;
import com.commerce.web.product.dto.response.ProductSimpleInfoResponse;
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
    public ResponseEntity<?> registerProduct(@RequestBody ProductRegisterRequestDto dto) {
        productService.productRegister(dto);
        return new ResponseEntity<>("상품 등록완료.", HttpStatus.OK);
    }

    // 상품 리스트 조회 API
    @GetMapping("/list")
    public ResponseEntity<?> getProducts(@RequestParam int page,
                                         @RequestParam int size) {
        ProductPageResponse<ProductSimpleInfoResponse> productList
                = productService.getProductList(page - 1, size);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    // 상품 상세 조회 API
    @GetMapping("/detail/{productId}")
    public ResponseEntity<?> detailProduct(@PathVariable("productId") Long productId) {
        ProductInfoResponse target = productService.getProductInfo(productId);
        return new ResponseEntity<>(target, HttpStatus.OK);
    }
}

