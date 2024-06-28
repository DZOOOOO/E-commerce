package com.commerce.domain.member.entity;

import com.commerce.domain.order.entity.Order;
import com.commerce.domain.order.entity.WishList;
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
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum role;

    // wishList
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<WishList> wishList = new ArrayList<>();

    // order
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orderList = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 주소, 전화번호 업데이트 메서드
    public void updateAddressPhone(String address, String phone) {
        this.address = address;
        this.phone = phone;
    }

    // 비밀번호 업데이트 메서드
    public void updatePassword(String password) {
        this.password = password;
    }
}
