package com.commerce.domain.order.service;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.member.repository.MemberRepository;
import com.commerce.domain.order.entity.Cart;
import com.commerce.domain.order.entity.CartItem;
import com.commerce.domain.order.entity.WishList;
import com.commerce.domain.order.repository.CartItemRepository;
import com.commerce.domain.order.repository.CartRepository;
import com.commerce.domain.order.repository.WishListRepository;
import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.repository.ProductRepository;
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
    private final CartItemRepository cartItemRepository;

    // wishList 등록.
    @Transactional
    public void wishRegister(Long productId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));
        Optional<WishList> findWishList = wishListRepository
                .findByMemberAndProductId(member, productId);
        if (findWishList.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 상품입니다.");
        }
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            wishListRepository.save(WishList.builder()
                    .member(member)
                    .productId(productId)
                    .build());
        } else {
            throw new IllegalArgumentException("상품이 없습니다. 확인후 다시 시도 해주세요.");
        }
    }

    // wishList 조회
    @Transactional(readOnly = true)
    public List<WishListResponseDto> viewWishList(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));
        List<WishList> wishList = member.getWishList();
        List<WishListResponseDto> result = new ArrayList<>();
        for (WishList p : wishList) {
            Optional<Product> product = productRepository.findById(p.getProductId());
            product.ifPresent(value -> result.add(WishListResponseDto.builder()
                    .id(value.getId())
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
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
        wishListRepository.deleteByIdAndMember(wishListId, member);
    }

    // 장바구니로 보내기
    @Transactional
    public void addToCart(String email, Long wishId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
        WishList target = wishListRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));
        long productId = target.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        CartItem cartItem = CartItem.builder()
                .cart(member.getCart())
                .productId(productId)
                .price(product.getProductPrice())
                .stock(product.getProductStock())
                .build();
        cartItemRepository.save(cartItem);
    }
}
