package com.commerce.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {

    BEFORE("구매확정X"),
    COMPLETE("구매확정O");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
