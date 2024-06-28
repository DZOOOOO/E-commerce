package com.commerce.domain.order.repository;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByMember(Member member);

    Optional<Order> findByProductId(Long productId);

    List<Order> findAllByMember(Member member, Pageable pageable);

    Optional<Order> findByIdAndMember(Long orderId, Member member);

    Page<Order> findByMemberEmail(String email, Pageable pageable);

}
