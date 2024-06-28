package com.commerce.domain.order.entity;

import com.commerce.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "product_id")
    private Long productId; // 주문 상품

    @Column(name = "total_amount")
    private Integer totalAmount; // 총 결제 금액

    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태여부

    @Column(name = "order_confirmation")
    private boolean orderConfirmation = false; // 구매 확정여부

    @Column(name = "order_cancel")
    private boolean orderCancel = false; // 주문취소

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 주문취소
    public void orderCancel() {
        this.orderCancel = true;
    }

    // 주문확정
    public void orderConfirmation() {
        this.orderConfirmation = true;
    }
}
