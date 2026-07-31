package com.khang.backendecommerce.domain.discount.discountpattern.impl;

import com.khang.backendecommerce.domain.discount.discountpattern.DiscountStrategy;

import java.math.BigDecimal;

public class FixedDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(BigDecimal subtotal, BigDecimal discountValue) {
        return discountValue.min(subtotal);
    }
}
