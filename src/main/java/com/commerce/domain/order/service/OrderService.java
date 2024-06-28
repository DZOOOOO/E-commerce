package com.commerce.domain.order.service;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.member.repository.MemberRepository;
import com.commerce.domain.order.entity.Order;
import com.commerce.domain.order.entity.OrderStatus;
import com.commerce.domain.order.repository.OrderRepository;
import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.repository.ProductRepository;
import com.commerce.web.exception.member.MemberException;
import com.commerce.web.exception.member.MemberExceptionCode;
import com.commerce.web.exception.order.OrderException;
import com.commerce.web.exception.order.OrderExceptionCode;
import com.commerce.web.exception.product.ProductException;
import com.commerce.web.exception.product.ProductExceptionCode;
import com.commerce.web.order.dto.request.OrderCheckOutRequestDto;
import com.commerce.web.order.dto.response.OrderPageResponse;
import com.commerce.web.order.dto.response.OrderStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    // 주문하기 -> 상품은 한번만 구매가 가능.(일회성 구매)
    @Transactional
    public void orderCheckOut(String email, OrderCheckOutRequestDto dto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));
        Optional<Order> orderProduct = orderRepository.findByProductId(dto.getProductId());
        if (orderProduct.isPresent()) {
            throw new OrderException(OrderExceptionCode.ORDER_ALREADY);
        }

        int totalPrice = dto.getBuyProductPrice() * dto.getProductQuantity();
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductException(ProductExceptionCode.NOT_FOUND));
        orderRepository.save(Order.builder()
                .member(member)
                .productId(product.getId())
                .totalAmount(totalPrice)
                .orderConfirmation(false)
                .orderStatus(OrderStatus.BEFORE)
                .build());
    }

    // 주문상황 조회
    @Transactional(readOnly = true)
    public OrderPageResponse<OrderStatusResponseDto> viewOrderList(String email, int page, int size) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);

        Page<Order> orderPage = orderRepository.findByMemberEmail(member.getEmail(), pageable);

        Page<OrderStatusResponseDto> orderStatusPage = orderPage.map(order -> {
            String productName = productRepository.findById(order.getProductId())
                    .map(Product::getProductName)
                    .orElse("없는 상품");
            return OrderStatusResponseDto.builder()
                    .orderDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .orderId(order.getId())
                    .productName(productName)
                    .totalPrice(order.getTotalAmount())
                    .orderStatus(OrderStatus.BEFORE)
                    .purchaseConfirm(false) // 구매확정 여부
                    .build();
        });

        return new OrderPageResponse<>(orderStatusPage);
    }

    // 주문취소
    @Transactional
    public void orderCancel(UserDetails userDetails, Long orderId) {
        String email = userDetails.getUsername();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));
        Order order = orderRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> new OrderException(OrderExceptionCode.NOT_FOUND));
        if (order.isOrderConfirmation()) {
            throw new OrderException(OrderExceptionCode.ORDER_NOT_CANCEL);
        }
        order.orderCancel();
        orderRepository.save(order);
    }

    // 주문 구매확정
    @Transactional
    public void purchaseConfirm(UserDetails userDetails, Long orderId) {
        String email = userDetails.getUsername();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));
        Order order = orderRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> new OrderException(OrderExceptionCode.NOT_FOUND));
        order.orderConfirmation();
        orderRepository.save(order);
    }

}
