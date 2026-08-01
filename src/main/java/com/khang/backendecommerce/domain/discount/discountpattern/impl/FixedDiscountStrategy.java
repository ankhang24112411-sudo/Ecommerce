package com.khang.backendecommerce.domain.discount.discountpattern.impl;

import com.khang.backendecommerce.domain.discount.discountpattern.DiscountContext;
import com.khang.backendecommerce.domain.discount.discountpattern.DiscountStrategy;
import com.khang.backendecommerce.domain.discount.entity.DiscountEntity;

import java.math.BigDecimal;

public class FixedDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(DiscountEntity discount, DiscountContext context) {
        return discount.getDiscountValue().min(context.subtotal());
    }
}
