package com.commerce.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {

    BEFORE("배송전"),
    DURING("배송전"),
    COMPLETE("배송완료");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
