package com.khang.backendecommerce.domain.discount.discountpattern;

import com.khang.backendecommerce.domain.discount.discountpattern.impl.FixedDiscountStrategy;
import com.khang.backendecommerce.domain.discount.discountpattern.impl.FreeshipDiscountStrategy;
import com.khang.backendecommerce.domain.discount.discountpattern.impl.PercentDiscountStrategy;
import com.khang.backendecommerce.infrastructure.common.enums.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscountFactory {
    private final FixedDiscountStrategy fixedDiscountStrategy;
    private final PercentDiscountStrategy percentDiscountStrategy;
    private final FreeshipDiscountStrategy freeshipDiscountStrategy;
    public DiscountStrategy getStrategy(DiscountType discountType) {

        return switch (discountType) {
            case FIXED_DISCOUNT -> fixedDiscountStrategy;
            case NEW_CUSTOMER ,BIRTHDAY_DISCOUNT , ONE_YEAR_ANNIVERSARY-> percentDiscountStrategy;
            case FREE_SHIP -> freeshipDiscountStrategy;
        };
    }
}
