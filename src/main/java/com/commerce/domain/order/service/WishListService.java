package com.commerce.domain.order.service;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.member.repository.MemberRepository;
import com.commerce.domain.order.entity.WishList;
import com.commerce.domain.order.repository.WishListRepository;
import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.repository.ProductRepository;
import com.commerce.web.exception.member.MemberException;
import com.commerce.web.exception.member.MemberExceptionCode;
import com.commerce.web.exception.product.ProductException;
import com.commerce.web.exception.product.ProductExceptionCode;
import com.commerce.web.exception.wishlist.WishListExceptionCode;
import com.commerce.web.exception.wishlist.WishlistException;
import com.commerce.web.order.dto.request.ProductOptionRequestDto;
import com.commerce.web.order.dto.response.OrderPageViewResponseDto;
import com.commerce.web.order.dto.response.WishListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j(topic = "WishListService")
@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // wishList 등록.
    @Transactional
    public void wishRegister(Long productId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));

        wishListRepository.findByMemberAndProductId(member, productId)
                .ifPresent(w -> {
                    throw new ProductException(ProductExceptionCode.ALREADY_REGISTER);
                });

        productRepository.findById(productId).ifPresentOrElse(
                p -> wishListRepository.save(WishList.builder()
                        .member(member)
                        .productId(productId)
                        .build()),
                () -> {
                    throw new ProductException(ProductExceptionCode.NOT_FOUND);
                });
    }

    // wishList 조회
    @Transactional(readOnly = true)
    public List<WishListResponseDto> viewWishList(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));
        List<WishList> wishList = member.getWishList();
        List<WishListResponseDto> result = new ArrayList<>();
        for (WishList p : wishList) {
            Optional<Product> product = productRepository.findById(p.getProductId());
            product.ifPresent(value -> result.add(WishListResponseDto.builder()
                    .productId(value.getId())
                    .productName(value.getProductName())
                    .price(value.getProductPrice())
                    .build()));
        }
        return result;
    }

    // wishList 삭제(선택 삭제)
    @Transactional
    public void deleteWishList(Long wishListId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));
        wishListRepository.deleteByIdAndMember(wishListId, member);
    }

    // wishList 주문 페이지 조회(한 종류의 상품만 구매 가능)
    @Transactional(readOnly = true)
    public OrderPageViewResponseDto wishToOrderPage(String email, Long wishId,
                                                    ProductOptionRequestDto dto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));
        WishList wishList = wishListRepository.findById(wishId)
                .orElseThrow(() -> new WishlistException(WishListExceptionCode.WISH_LIST_NOT_FOUND));
        Product product = productRepository.findById(wishList.getProductId())
                .orElseThrow(() -> new ProductException(ProductExceptionCode.NOT_FOUND));

        return OrderPageViewResponseDto.builder()
                .memberId(member.getId())
                .productId(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productQuantity(dto.getProductQuantity())
                .totalPrice(dto.getTotalPrice())
                .build();
    }

}
