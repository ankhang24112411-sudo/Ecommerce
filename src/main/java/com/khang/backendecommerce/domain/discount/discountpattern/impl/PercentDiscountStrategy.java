package com.khang.backendecommerce.domain.discount.discountpattern.impl;

import com.khang.backendecommerce.domain.discount.discountpattern.DiscountContext;
import com.khang.backendecommerce.domain.discount.discountpattern.DiscountStrategy;
import com.khang.backendecommerce.domain.discount.entity.DiscountEntity;

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
