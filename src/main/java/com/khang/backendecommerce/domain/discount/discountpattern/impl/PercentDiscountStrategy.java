package com.khang.backendecommerce.domain.discount.discountpattern.impl;

import com.khang.backendecommerce.domain.discount.discountpattern.DiscountStrategy;

import java.math.BigDecimal;

public class PercentDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(BigDecimal subtotal, BigDecimal discountValue) {
        return subtotal
                .multiply(discountValue)
                .divide(BigDecimal.valueOf(100));
    }
}
