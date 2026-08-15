package com.khang.backendecommerce.infrastructure.discountinfra.impl;

import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountStrategy;
import com.khang.backendecommerce.newstruc.entity.DiscountEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FixedDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(DiscountEntity discount, DiscountContext context) {
        return discount.getDiscountValue().min(context.subtotal());
    }
}
