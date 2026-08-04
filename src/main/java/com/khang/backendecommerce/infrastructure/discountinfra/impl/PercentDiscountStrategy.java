package com.khang.backendecommerce.infrastructure.discountinfra.impl;

import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountStrategy;
import com.khang.backendecommerce.newstruc.entity.DiscountEntity;

import java.math.BigDecimal;

public class PercentDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(DiscountEntity discount, DiscountContext context) {
        return context.subtotal().multiply(discount.getDiscountValue()).divide(BigDecimal.valueOf(100));
//        return subtotal
//                .multiply(discountValue)
//                .divide(BigDecimal.valueOf(100));
    }
}
