package com.commerce.domain.product.service;

import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.repository.ProductRepository;
import com.commerce.web.exception.product.ProductException;
import com.commerce.web.exception.product.ProductExceptionCode;
import com.commerce.web.product.dto.request.ProductRegisterRequestDto;
import com.commerce.web.product.dto.response.ProductInfoResponse;
import com.commerce.web.product.dto.response.ProductPageResponse;
import com.commerce.web.product.dto.response.ProductSimpleInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "ProductService")
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록.
    @Transactional
    public void productRegister(ProductRegisterRequestDto dto) {
        Product product = Product.builder()
                .productName(dto.getProductName())
                .productPrice(dto.getProductPrice())
                .productStock(dto.getProductStock())
                .productDescription(dto.getProductDescription())
                .productCategory(dto.getProductCategory())
                .productPurchaseStatus(true) // 기본적으로 구매 가능.
                .build();
        productRepository.save(product);
    }

    // 등록된 상품 리스트 보여주기.
    @Transactional(readOnly = true)
    public ProductPageResponse<ProductSimpleInfoResponse> getProductList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductSimpleInfoResponse> productPage = productRepository.findAll(pageable)
                .map(p -> ProductSimpleInfoResponse
                        .builder()
                        .productId(p.getId())
                        .productName(p.getProductName())
                        .productPrice(p.getProductPrice())
                        .productStock(p.getProductStock())
                        .productPurchaseStatus(p.isProductPurchaseStatus())
                        .build());
        return new ProductPageResponse<>(productPage);
    }

    // 상품 상세정보
    @Transactional(readOnly = true)
    public ProductInfoResponse getProductInfo(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductExceptionCode.NOT_FOUND));
        return ProductInfoResponse.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productStock(product.getProductStock())
                .productDescription(product.getProductDescription())
                .productPurchaseStatus(product.isProductPurchaseStatus())
                .build();
    }
}
