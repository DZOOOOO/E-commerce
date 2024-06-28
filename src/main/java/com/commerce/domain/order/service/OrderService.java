package com.commerce.domain.order.service;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.member.repository.MemberRepository;
import com.commerce.domain.order.repository.OrderRepository;
import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public void orderCheckOut(String email, Long productId) {
        Member customer = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));
        Product orderProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("없는 상품입니다."));
    }
}
