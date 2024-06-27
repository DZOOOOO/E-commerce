package com.commerce.web.order.controller;

import com.commerce.domain.order.service.WishListService;
import com.commerce.web.order.dto.response.WishListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "WishListController")
@RestController
@RequestMapping("/api/wish")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    // 위시리스트 등록 API
    @PostMapping("/{productId}")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> registerWishProduct(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long productId) {
        String email = userDetails.getUsername();
        wishListService.wishRegister(productId, email);
        return new ResponseEntity<>("WishList 등록 완료..!", HttpStatus.OK);
    }

    // 위시리스트 조회 API
    @GetMapping
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getWishList(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<WishListResponseDto> result = wishListService.viewWishList(email);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 위시리스트 등록된 상품 장바구니 담기 API
    @PostMapping("/cart/{wishId}")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> orderWishList(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable Long wishId) {
        String email = userDetails.getUsername();
        wishListService.addToCart(email, wishId);
        return new ResponseEntity<>("장바구니 이동..!",HttpStatus.OK);
    }

    // 위시리스트 내 항목 삭제 API (선택 -> 삭제)
    @DeleteMapping("/{wishId}")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> deleteWishList(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable Long wishId) {
        String email = userDetails.getUsername();
        wishListService.deleteWishList(wishId, email);
        return new ResponseEntity<>("삭제 완료..!", HttpStatus.OK);
    }
}
