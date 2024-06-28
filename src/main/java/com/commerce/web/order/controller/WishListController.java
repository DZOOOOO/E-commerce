package com.commerce.web.order.controller;

import com.commerce.domain.order.service.WishListService;
import com.commerce.web.order.dto.request.ProductOptionRequestDto;
import com.commerce.web.order.dto.response.OrderPageViewResponseDto;
import com.commerce.web.order.dto.response.WishListResponseDto;
import com.commerce.web.order.dto.response.WishResponseDto;
import com.commerce.web.order.dto.response.message.WishListMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
                                                 @Positive @PathVariable Long productId) {
        String email = userDetails.getUsername();
        wishListService.wishRegister(productId, email);
        return new ResponseEntity<>(WishResponseDto.builder()
                .message(WishListMessage.WISH_LIST_REGISTER)
                .build(), HttpStatus.OK);
    }

    // 위시리스트 조회 API
    @GetMapping
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getWishList(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<WishListResponseDto> result = wishListService.viewWishList(email);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 위시리스트 내 항목 삭제 API (선택 -> 삭제)
    @DeleteMapping("/{wishId}")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> deleteWishList(@AuthenticationPrincipal UserDetails userDetails,
                                            @Positive @PathVariable Long wishId) {
        String email = userDetails.getUsername();
        wishListService.deleteWishList(wishId, email);
        return new ResponseEntity<>(WishResponseDto.builder()
                .message(WishListMessage.WISH_LIST_DELETE)
                .build(), HttpStatus.OK);
    }

    // 위시리스트에 있는 상품 주문 페이지로 이동 API (한 종류의 상품만 주문이 가능하다.)
    @PostMapping("/{wishId}/order")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> wishToOrderPage(@AuthenticationPrincipal UserDetails userDetails,
                                             @Positive @PathVariable Long wishId,
                                             @Valid @RequestBody ProductOptionRequestDto dto) {
        String email = userDetails.getUsername();
        OrderPageViewResponseDto response = wishListService.wishToOrderPage(email, wishId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
