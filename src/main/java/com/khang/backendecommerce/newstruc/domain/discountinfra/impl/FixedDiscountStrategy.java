package com.khang.backendecommerce.newstruc.domain.discountinfra.impl;

import com.khang.backendecommerce.newstruc.domain.discountinfra.DiscountContext;
import com.khang.backendecommerce.newstruc.domain.discountinfra.DiscountStrategy;
import com.khang.backendecommerce.newstruc.entity.DiscountEntity;

import java.math.BigDecimal;

public class FixedDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(DiscountEntity discount, DiscountContext context) {
        return discount.getDiscountValue().min(context.subtotal());
    }
}
