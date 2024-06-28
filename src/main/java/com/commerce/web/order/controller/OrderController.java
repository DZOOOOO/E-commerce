package com.commerce.web.order.controller;

import com.commerce.domain.order.service.OrderService;
import com.commerce.web.order.dto.request.OrderCheckOutRequestDto;
import com.commerce.web.order.dto.response.message.OrderMessage;
import com.commerce.web.order.dto.response.OrderPageResponse;
import com.commerce.web.order.dto.response.OrderResponseDto;
import com.commerce.web.order.dto.response.OrderStatusResponseDto;
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

@Slf4j(topic = "OrderController")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문하기 API
    @PostMapping("/checkout")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> checkOut(@AuthenticationPrincipal UserDetails userDetails,
                                      @Valid @RequestBody OrderCheckOutRequestDto dto) {
        orderService.orderCheckOut(userDetails.getUsername(), dto);

        return new ResponseEntity<>(OrderResponseDto.builder()
                .message(OrderMessage.ORDER_COMPLETE)
                .build(), HttpStatus.OK);
    }

    // 주문내역 조회 API
    @GetMapping("/list")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> viewOrderList(@AuthenticationPrincipal UserDetails userDetails,
                                           @Positive @RequestParam int page,
                                           @Positive @RequestParam int size) {
        String email = userDetails.getUsername();
        OrderPageResponse<OrderStatusResponseDto> response
                = orderService.viewOrderList(email, page - 1, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 주문취소 API
    @GetMapping("/cancel/{orderId}")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> orderCancel(@AuthenticationPrincipal UserDetails userDetails,
                                         @Positive @PathVariable Long orderId) {
        orderService.orderCancel(userDetails, orderId);
        return new ResponseEntity<>(OrderResponseDto.builder()
                .message(OrderMessage.ORDER_CANCEL)
                .build(), HttpStatus.OK);
    }

    // 주문 구매확정 API
    @GetMapping("/confirm/{orderId}")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> orderConfirm(@AuthenticationPrincipal UserDetails userDetails,
                                          @Positive @PathVariable Long orderId) {
        orderService.purchaseConfirm(userDetails, orderId);
        return new ResponseEntity<>(OrderResponseDto.builder()
                .message(OrderMessage.ORDER_CONFIRM)
                .build(), HttpStatus.OK);
    }

}
