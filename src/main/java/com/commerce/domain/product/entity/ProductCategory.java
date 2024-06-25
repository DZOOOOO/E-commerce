package com.commerce.domain.product.entity;

import lombok.Getter;


@Getter
public enum ProductCategory {

    // 커피 종류 -> 브랜드 추가 예정.
    INSTANT(),
    CONCENTRATE(),// 원액
    DRIP_BAG(),
    CAPSULE(), // 캡슐
    COLD_BREW(),
    SYRUP(),
    BASE()

}
